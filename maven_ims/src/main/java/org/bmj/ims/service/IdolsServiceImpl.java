package org.bmj.ims.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bmj.ims.dao.GroupsDAO;
import org.bmj.ims.dao.IdolsDAO;
import org.bmj.ims.dao.LikesDAO;
import org.bmj.ims.util.PaginateUtil;
import org.bmj.ims.vo.Group;
import org.bmj.ims.vo.Idol;
import org.bmj.ims.vo.Like;
import org.bmj.ims.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class IdolsServiceImpl implements IdolsService{
	
	@Autowired
	private IdolsDAO idolsDAO;
	
	@Autowired
	private LikesDAO likesDAO;
	
	@Autowired
	private GroupsDAO groupsDAO;

	@Override
	public Map<String,Object> getIdols(int page) {
		
		Map<String, Object> map=new ConcurrentHashMap<String, Object>();
		
		int total=idolsDAO.selectTotal();
		
		PageVO pageVo=new PageVO(page,5);
		
		map.put("idols", idolsDAO.selectList(pageVo));
		map.put("paginate",PaginateUtil.getPaginate(page, total, 5, 3, "/idol"));
		
		return map;
	}
	
	@Override
	public int deleteIdol(int idolId) {
		return idolsDAO.delete(idolId);
	}
	
	@Override
	public Idol getIdol(int idolId, int memberNo) {
		
		Map<String, Object> map = new ConcurrentHashMap<String, Object>();
		
		map.put("idolId", idolId);
		map.put("memberNo", memberNo);
		
		return idolsDAO.selectOne(map);
	}
	
	@Override
	public List<Group> getGroups() {
		return groupsDAO.selectGroupList();
	}
	
	@Override
	public void registerIdol(Idol idol) {
		idolsDAO.insert(idol);
	}
	
	@Override
	public Map<String, Object> getIdolUpdateForm(int idolId) {
		Map<String, Object> map = 
				new HashMap<String, Object>();
		
		map.put("idol",idolsDAO.selectOne(idolId));
		map.put("groups",groupsDAO.selectGroupList());
		
		return map;
	}
	
	@Override
	public void updateIdol(Idol idol) {
		idolsDAO.update(idol);
	}
	
	@Transactional
	@Override
	public void likeIdol(Like like) {
		
		Integer likeNo = likesDAO.selectLikeNo(like);
		
		if(likeNo==null) {
			likesDAO.insert(like);
		}else {
			likesDAO.delete(likeNo);
		}//if else end
		
		idolsDAO.updateLikeCount(like.getTypeNo());
		
	}
	
}



