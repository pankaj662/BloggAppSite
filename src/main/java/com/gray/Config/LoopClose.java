package com.gray.Config;

import org.springframework.stereotype.Component;

@Component
public class LoopClose {

	public volatile boolean running=true;
}
