package com.itandroid.zhbj.domain;

import java.util.ArrayList;

public class PhotoBean {
  public PhotoData data;
  
  public class PhotoData {
	  public ArrayList<PhotoNews> news;
  }
  public class PhotoNews{
	  public int id;
	  public String listimage;
	  public String title;
  }
}
