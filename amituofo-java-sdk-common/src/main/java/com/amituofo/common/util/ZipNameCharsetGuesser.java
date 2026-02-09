package com.amituofo.common.util;
import java.nio.ByteBuffer;
import java.nio.charset.*;
import java.util.*;

public final class ZipNameCharsetGuesser {

    private ZipNameCharsetGuesser() {}

    /**
     * ZIP 文件名常见候选编码（按优先级）
     */
    private static final Charset[] CANDIDATES = new Charset[] {
            StandardCharsets.UTF_8,
            Charset.forName("GBK"),
            Charset.forName("Big5"),
            Charset.forName("Shift_JIS"),
            StandardCharsets.ISO_8859_1
    };

    /**
     * 猜测 raw entry name bytes 的最可能编码
     */
    public static Charset guess(byte[] nameBytes) {

        Charset bestCharset = null;
        int bestScore = Integer.MIN_VALUE;

        for (Charset cs : CANDIDATES) {

            // UTF-8 必须先过“合法性校验”
            if (cs == StandardCharsets.UTF_8 && !isValidUtf8(nameBytes)) {
                continue;
            }

            String decoded = decode(nameBytes, cs);
            int score = score(decoded);

            if (score > bestScore) {
                bestScore = score;
                bestCharset = cs;
            }
        }

        return bestCharset != null ? bestCharset : Charset.defaultCharset();
    }

    // =========================
    //  decoding
    // =========================

    private static String decode(byte[] bytes, Charset cs) {
        return new String(bytes, cs);
    }

    /**
     * UTF-8 严格合法性检测（不是 new String）
     */
    private static boolean isValidUtf8(byte[] bytes) {
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT);
        try {
            decoder.decode(ByteBuffer.wrap(bytes));
            return true;
        } catch (CharacterCodingException e) {
            return false;
        }
    }

    // =========================
    //  scoring
    // =========================

    /**
     * 给“解码后的文件名”打分
     */
    private static int score(String s) {

        if (s == null || s.isEmpty()) {
            return Integer.MIN_VALUE;
        }

        int score = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            // replacement char：强烈乱码
            if (c == '\uFFFD') {
                score -= 100;
                continue;
            }

            // 控制字符（非法文件名）
            if (c < 32 && c != '\t') {
                score -= 50;
                continue;
            }

            // Windows 非法字符
            if ("<>:\"|?*".indexOf(c) >= 0) {
                score -= 20;
            }

            // 常见 UTF-8→ISO 乱码模式
            if ("ÃÂ¤Â¸Â".indexOf(c) >= 0) {
                score -= 5;
            }

            // CJK 文字：强加分
            if (isCJK(c)) {
                score += 10;
                continue;
            }

            // 可读 ASCII
            if (Character.isLetterOrDigit(c)) {
                score += 2;
                continue;
            }

            // 常见文件名符号
            if ("-_.()[] ".indexOf(c) >= 0) {
                score += 1;
                continue;
            }

            // 其他符号：轻微扣分
            score -= 1;
        }

        // 长度合理性（极短 or 极怪）
        if (s.length() <= 1) {
            score -= 10;
        }

        return score;
    }

    private static boolean isCJK(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        return block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || block == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || block == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B;
    }
}
