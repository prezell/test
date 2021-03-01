package org.bmj.ims.dao;

import org.bmj.ims.vo.Like;

public interface LikesDAO {
	
	public int insert(Like like);

	public Integer selectLikeNo(Like like);

	public void delete(int no);

}
