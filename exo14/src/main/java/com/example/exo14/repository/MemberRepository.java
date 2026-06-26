package com.example.exo14.repository;

import com.example.exo14.model.Member;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findById(String id);
    Member save(Member member);
}
