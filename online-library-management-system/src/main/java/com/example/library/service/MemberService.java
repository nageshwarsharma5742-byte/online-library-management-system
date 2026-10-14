package com.example.library.service;

import com.example.library.entity.Member;
import com.example.library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public Member addMember(Member member) {
        return memberRepository.save(member);
    }

    public Member createMember(Member member) {
        return addMember(member);
    }

    public Member updateMember(Long id, Member memberDetails) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
        member.setName(memberDetails.getName());
        member.setEmail(memberDetails.getEmail());
        member.setPhoneNumber(memberDetails.getPhoneNumber());
        return memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Optional<Member> getMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}