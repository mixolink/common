package com.amituofo.common.util;

import java.util.*;
import java.util.regex.Pattern;

public class VersionUtils {

    private static final Pattern VERSION_PATTERN = Pattern.compile("^[\\dA-Za-z.-]+$");

    /**
     * 比较两个版本号，支持 null 和空字符串
     *
     * @return 1 if v1 > v2; -1 if v1 < v2; 0 if equal
     */
    public static int compareVersions(String v1, String v2) {
        String normV1 = normalizeVersion(v1);
        String normV2 = normalizeVersion(v2);

        boolean valid1 = normV1 != null;
        boolean valid2 = normV2 != null;

        if (!valid1 && !valid2) return 0;
        if (!valid1) return -1;
        if (!valid2) return 1;

        return compareValidVersions(normV1, normV2);
    }

    private static String normalizeVersion(String version) {
        if (version == null) return null;
        String trimmed = version.trim();
        return trimmed.isEmpty() ? null : trimmed.toLowerCase();
    }

    private static int compareValidVersions(String v1, String v2) {
        if (!VERSION_PATTERN.matcher(v1).matches() || !VERSION_PATTERN.matcher(v2).matches()) {
            throw new IllegalArgumentException("Invalid version format: '" + v1 + "' or '" + v2 + "'");
        }

        // 解析为结构化版本对象
        ParsedVersion ver1 = parseVersion(v1);
        ParsedVersion ver2 = parseVersion(v2);

        // 1. 比较主版本 (MAJOR.MINOR.PATCH)
        int mainCompare = compareMain(ver1.mainParts, ver2.mainParts);
        if (mainCompare != 0) {
            return mainCompare;
        }

        // 2. 比较预发布标识（PRERELEASE）
        return comparePrerelease(ver1.prerelease, ver2.prerelease);
    }

    // --- 解析与比较逻辑 ---

    private static ParsedVersion parseVersion(String version) {
        // 分离主版本 和 预发布/构建信息（只取第一个 '-' 或 '+' 前为主版本）
        int idx1 = version.indexOf('-');
        int idx2 = version.indexOf('+');
        int splitIdx = -1;
        if (idx1 >= 0 && idx2 >= 0) {
            splitIdx = Math.min(idx1, idx2);
        } else if (idx1 >= 0) {
            splitIdx = idx1;
        } else if (idx2 >= 0) {
            splitIdx = idx2;
        }

        String mainPart = splitIdx >= 0 ? version.substring(0, splitIdx) : version;
        String prerelease = splitIdx >= 0 && version.charAt(splitIdx) == '-' 
            ? version.substring(splitIdx + 1).split("\\+", 2)[0]  // 忽略 +build
            : null;

        // 补全主版本为至少3段（MAJOR.MINOR.PATCH）
        String[] mainParts = mainPart.split("\\.");
        if (mainParts.length < 3) {
            String[] extended = new String[3];
            System.arraycopy(mainParts, 0, extended, 0, mainParts.length);
            for (int i = mainParts.length; i < 3; i++) {
                extended[i] = "0";
            }
            mainParts = extended;
        }

        return new ParsedVersion(mainParts, prerelease);
    }

    private static int compareMain(String[] v1, String[] v2) {
        for (int i = 0; i < 3; i++) {
            int num1 = tryParseInt(v1[i]);
            int num2 = tryParseInt(v2[i]);
            if (num1 != num2) {
                return Integer.compare(num1, num2);
            }
        }
        return 0;
    }

    private static int comparePrerelease(String pre1, String pre2) {
        boolean hasPre1 = pre1 != null && !pre1.isEmpty();
        boolean hasPre2 = pre2 != null && !pre2.isEmpty();

        // 正式版 > 预发布版
        if (!hasPre1 && hasPre2) return 1;
        if (hasPre1 && !hasPre2) return -1;
        if (!hasPre1 && !hasPre2) return 0;

        // 两者都是预发布：按 SemVer 规则逐段比较
        String[] parts1 = pre1.split("\\.");
        String[] parts2 = pre2.split("\\.");

        int len = Math.max(parts1.length, parts2.length);
        for (int i = 0; i < len; i++) {
            String p1 = i < parts1.length ? parts1[i] : "";
            String p2 = i < parts2.length ? parts2[i] : "";

            boolean isNum1 = p1.matches("\\d+");
            boolean isNum2 = p2.matches("\\d+");

            if (isNum1 && isNum2) {
                // 数字按数值比较
                int n1 = Integer.parseInt(p1);
                int n2 = Integer.parseInt(p2);
                if (n1 != n2) return Integer.compare(n1, n2);
            } else if (!isNum1 && !isNum2) {
                // 都是非数字，按 ASCII 字典序
                int cmp = p1.compareTo(p2);
                if (cmp != 0) return cmp;
            } else {
                // 数字 < 非数字（SemVer 规定）
                return isNum1 ? -1 : 1;
            }
        }
        return 0;
    }

    private static int tryParseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0; // 非数字视为 0（保守处理）
        }
    }

    // --- 内部类 ---

    private static class ParsedVersion {
        final String[] mainParts;   // [major, minor, patch]
        final String prerelease;    // 如 "beta.1", "alpha", null 表示正式版

        ParsedVersion(String[] mainParts, String prerelease) {
            this.mainParts = mainParts;
            this.prerelease = prerelease;
        }
    }

    // --- 便捷方法 ---
    public static boolean isNewer(String v1, String v2) {
        return compareVersions(v1, v2) > 0;
    }

    public static boolean isSameOrNewer(String v1, String v2) {
        return compareVersions(v1, v2) >= 0;
    }

    // --- 测试用例（可选）---
    public static void main(String[] args) {
        // 测试用例
        assert compareVersions("4.1", "4.2-beta.1") < 0;
        assert compareVersions("3.1.1", "3.1.2") < 0;
        assert compareVersions("3.1-beta", "3.1-alpha") > 0;
        assert compareVersions("5.0-rc.1", "5.0") < 0;
        assert compareVersions("2.0.0-beta.2", "2.0.0-beta.10") < 0; // 数值比较
        assert compareVersions("1.0.0", "1.0") == 0;
        assert compareVersions("1.0.0-alpha", "1.0.0-alpha.1") < 0;

        System.out.println("All tests passed.");
    }
}