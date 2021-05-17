package com.mms.cases.leetcode.link;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 给你两个 非空 链表来代表两个非负整数。数字最高位位于链表开始位置。它们的每个节点只存储一位数字。将这两数相加会返回一个新的链表。
 *
 * 你可以假设除了数字 0 之外，这两个数字都不会以零开头。 
 *
 * 进阶：
 *
 * 如果输入链表不能修改该如何处理？换句话说，你不能对列表中的节点进行翻转。
 *
 * 示例：
 *
 * 输入：(7 -> 2 -> 4 -> 3) + (5 -> 6 -> 4)
 * 输出：7 -> 8 -> 0 -> 7
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/add-two-numbers-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution445 {

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        Deque<Integer> d1 = new LinkedList<Integer>();
        Deque<Integer> d2 = new LinkedList<Integer>();

        while (l1 != null){
            d1.push(l1.data);
            l1 = l1.next;
        }

        while (l2 != null){
            d2.push(l2.data);
            l2 = l2.next;
        }

        int carry = 0;
        ListNode ns = null;

        while(!d1.isEmpty() || !d2.isEmpty() || carry != 0){
            int a = d1.pop();
            int b = d2.pop();
            int cur = a + b + carry;
            carry = cur/10;
            cur = cur%10;
            ListNode curNode = new ListNode(cur);
            curNode.next = ns;
            ns = curNode;
        }
        return null;
    }

    class ListNode{

        Integer data;
        ListNode next;

        public ListNode(Integer data) {
            this.data = data;
        }
    }
}
