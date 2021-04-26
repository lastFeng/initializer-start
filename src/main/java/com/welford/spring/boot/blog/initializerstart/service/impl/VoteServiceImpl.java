package com.welford.spring.boot.blog.initializerstart.service.impl;

import com.welford.spring.boot.blog.initializerstart.domain.Vote;
import com.welford.spring.boot.blog.initializerstart.mapper.VoteRepository;
import com.welford.spring.boot.blog.initializerstart.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : guoweifeng
 * @date : 2021/4/25
 */
@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Override
    public Vote getVoteById(Long id) {
        return voteRepository.getOne(id);
    }

    @Override
    public void removeVote(Long id) {
        voteRepository.deleteById(id);
    }
}
