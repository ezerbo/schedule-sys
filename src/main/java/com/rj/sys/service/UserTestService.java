package com.rj.sys.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rj.sys.dao.UserTestDao;

@Slf4j
@Service
public class UserTestService {

	private @Autowired UserTestDao userTestDao;
	
	
}
