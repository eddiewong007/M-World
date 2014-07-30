package com.mworld.ui.main;

import java.util.ArrayList;

import com.mworld.weibo.entities.Status;

public class TimelineInfo {

	public ArrayList<Status> statuses;

	public long since_id = 0;

	public long init_id = 0;

	public int page = 2;

	public TimelineInfo(ArrayList<Status> statuses) {
		super();
		this.statuses = statuses;
	}

}
