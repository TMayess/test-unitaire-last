package com.example.exo14.repository;

import com.example.exo14.model.Member;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryMemberRepository implements MemberRepository {

    private final ConcurrentHashMap<String, Member> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Member> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Member save(Member member) {
        store.put(member.getId(), member);
        return member;
    }
}
