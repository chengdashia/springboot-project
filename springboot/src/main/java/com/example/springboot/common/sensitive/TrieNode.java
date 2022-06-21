package com.example.springboot.common.sensitive;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 成大事
 * @since 2022/5/21 15:02
 */
public class TrieNode {
    // 关键词结束标识
    private boolean isKeywordEnd = false;

    // 子节点(key是下级字符,value是下级节点)
    private Map<Character, TrieNode> subNodes = new HashMap<>();

    public boolean isKeywordEnd() {
        return isKeywordEnd;
    }

    public void setKeywordEnd(boolean keywordEnd) {
        isKeywordEnd = keywordEnd;
    }

    // 添加子节点
    public void addSubNode(Character c, TrieNode node) {
        subNodes.put(c, node);
    }

    // 获取子节点
    public TrieNode getSubNode(Character c) {
        return subNodes.get(c);
    }
}
