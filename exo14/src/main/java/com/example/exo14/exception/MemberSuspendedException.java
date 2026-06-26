package com.example.exo14.exception;

public class MemberSuspendedException extends RuntimeException {
    public MemberSuspendedException(String memberId) {
        super("Member suspended: " + memberId);
    }
}
