package com.github.liuweijw.admin.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.liuweijw.admin.domain.Menu;
import com.github.liuweijw.admin.service.MenuService;
import com.github.liuweijw.core.beans.system.AuthMenu;
import com.github.liuweijw.core.commons.jwt.JwtUtil;
import com.github.liuweijw.core.commons.web.BaseController;

@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController {

	@Autowired
	private MenuService menuService;
	
	/**
     * 通过用户名查询用户菜单
     */
    @GetMapping("/findMenuByRole/{roleCode}")
    public Set<AuthMenu> findMenuByRole(@PathVariable String roleCode) {
    	Set<Menu> mList = menuService.findMenuByRole(roleCode);
    	Set<AuthMenu> rList = new HashSet<AuthMenu>();
    	if(null == mList || mList.size() == 0) return rList;
    	
    	for(Menu m : mList){
    		AuthMenu authMenu = new AuthMenu();
    		BeanUtils.copyProperties(m, authMenu);
    		rList.add(authMenu);
    	}
        return rList;
    }
    
    /**
     * 返回当前用户树形菜单集合
     * 
     * @return 树形菜单
     */
    @GetMapping("/userTree")
    public List<Integer> userTree(HttpServletRequest request) {
    	String roleCode = JwtUtil.getRole(request).get(0);
        Set<Menu> menus = menuService.findMenuByRole(roleCode);
       
        List<Integer> menuList = new ArrayList<>();
        if(null == menus || menus.size() == 0) return menuList;
        
        menus.stream().forEach(menu -> {
        	menuList.add(menu.getMenuId());
        });
        
        return menuList;
    }
}
