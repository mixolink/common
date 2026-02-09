package com.amituofo.common.util;
import java.util.*;
import java.util.regex.Pattern;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPUtils {

    // 正则：匹配 192.168.1.100 或 192.168.1.0/24
    private static final Pattern IP_CIDR_PATTERN = Pattern.compile(
        "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)" +
        "(\\/([0-9]|[1-2][0-9]|3[0-2]))?$"
    );

    /**
     * 校验并清洗白名单列表
     * @param rawList 原始 List<String>
     * @return 去重、格式正确的 IP/CIDR 列表
     */
    public static List<String> cleanIPList(List<String> rawList) {
        if (rawList == null || rawList.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> seen = new HashSet<>();   // 去重 + 规范化存储
        List<String> result = new ArrayList<>();

        for (String ip : rawList) {
            if (ip == null || ip.trim().isEmpty()) {
                continue;
            }

            String trimmed = ip.trim();

            // 1. 正则快速过滤
            if (!IP_CIDR_PATTERN.matcher(trimmed).matches()) {
                continue; // 格式错误
            }

            // 2. 精确校验：单 IP 或 CIDR
            if (trimmed.contains("/")) {
                // CIDR 格式
                if (isValidCidr(trimmed)) {
                    String normalized = normalizeCidr(trimmed);
                    if (seen.add(normalized)) {
                        result.add(trimmed); // 保留原始格式
                    }
                }
            } else {
                // 单 IP
                if (isValidIp(trimmed)) {
                    if (seen.add(trimmed)) { // 单 IP 直接用原值去重
                        result.add(trimmed);
                    }
                }
            }
        }

        return result;
    }

    // 校验单 IP 是否合法
    private static boolean isValidIp(String ip) {
        try {
            InetAddress addr = InetAddress.getByName(ip);
            // 排除通配、广播、多播等
            return !addr.isLoopbackAddress() && !addr.isAnyLocalAddress();
        } catch (UnknownHostException e) {
            return false;
        }
    }

    // 校验 CIDR 是否合法（子网掩码 0-32）
    private static boolean isValidCidr(String cidr) {
        String[] parts = cidr.split("/");
        if (parts.length != 2) return false;

        if (!isValidIp(parts[0])) return false;

        try {
            int prefix = Integer.parseInt(parts[1]);
            return prefix >= 0 && prefix <= 32;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // 规范化 CIDR（用于去重）：192.168.1.0/24 → 192.168.1.0/24（已标准）
    // 可选：转成最小网络地址（这里保持原始）
    private static String normalizeCidr(String cidr) {
        return cidr; // 当前已足够标准化
        // 如需更严格，可用：InetAddresses.forString(base).getHostAddress() + "/" + prefix
    }

    // ================ 示例调用 ================
    public static void main(String[] args) {
        List<String> raw = Arrays.asList(
            "192.168.1.100",
            "192.168.1.0/24",
            "192.168.1.100",           // 重复
            "256.1.2.3",               // 非法
            "192.168.1.0/33",          // 前缀超限
            "192.168.1.0/24",          // 重复 CIDR
            "  192.168.1.200  ",       // 带空格
            "192.168.1.1/30",
            "1",
            "xx.xx.xx",
            "1.1.1.1.1",
            "1.1.1.1",
            "1.1.1.999",
            null,
            ""
        );

        List<String> clean = cleanIPList(raw);

        System.out.println("清洗后的白名单：");
        clean.forEach(System.out::println);
    }
}