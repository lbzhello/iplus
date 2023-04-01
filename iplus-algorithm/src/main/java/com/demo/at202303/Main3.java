package com.demo.at202303;

import java.util.List;

public class Main3 {
    public static void main(String[] args) {

    }

    /**
     * 删除链表倒数第 n 个元素
     * @param n
     */
    public static ListNode f(ListNode head, int n) {
        if (head == null) {
            return null;
        }
        ListNode needDelPre = head; // 待删除节点的前一个节点
        ListNode cur = head;
        int i = 0;
        for (; i <= n - 1; i++) {
            cur = cur.getNext();
            if (cur == null) {
                break;
            }
        }

        // 删除第一个元素时
        if (cur == null && i == n) {
            return head.getNext();
        }

        // 长度不够，不删
        if (cur == null || i != n) {
            return head;
        }

        while (cur.getNext() != null) {
            cur = cur.getNext();
            needDelPre = needDelPre.getNext();
        }

        // 删除后一个节点
        ListNode needDel = needDelPre.getNext(); // 待删除的节点
        ListNode needDelNext = needDel.getNext(); // 待删除的后一个节点
        needDelPre.setNext(needDelNext);
        return head;
    }
}
