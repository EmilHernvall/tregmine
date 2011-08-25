package info.tregmine.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.tregmine.quadtree.Point;
import info.tregmine.quadtree.Rectangle;

public class Zone 
{
	public enum Permission
	{
		// can modify the zone in any way
		Owner,
		// can build in the zone
		Builder,
		// is allowed in the zone, if this isn't the default
		Allowed,
		// banned from the zone
		Banned;
		
		public static Permission fromString(String type)
		{
			if ("owner".equalsIgnoreCase(type)) {
				return Permission.Owner;
			} else if ("builder".equalsIgnoreCase(type)) {
				return Permission.Builder;
			} else if ("allowed".equalsIgnoreCase(type)) {
				return Permission.Allowed;
			} else if ("banned".equalsIgnoreCase(type)) {
				return Permission.Banned;
			}
			
			return null;
		}
	}
	
	private int id;
	private String name;
	private List<Rectangle> rects;
	
	private String textEnter;
	private String textExit;
	
	private Map<String, Permission> users;
	
	public Zone()
	{
		rects = new ArrayList<Rectangle>();
		users = new HashMap<String, Permission>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Rectangle> getRects() {
		return rects;
	}

	public void addRect(Rectangle rect) {
		rects.add(rect);
	}

	public void setRects(List<Rectangle> rects) {
		this.rects = rects;
	}
	
	public String getTextEnter() {
		return textEnter;
	}

	public void setTextEnter(String textEnter) {
		this.textEnter = textEnter;
	}

	public String getTextExit() {
		return textExit;
	}

	public void setTextExit(String textExit) {
		this.textExit = textExit;
	}
	
	public void setUsers(Map<String, Permission> v) {
		this.users = v;
	}
	
	public void addUser(String name, Permission perm) {
		users.put(name, perm);
	}
	
	public Permission getUser(String name) {
		return users.get(name);
	}
	
	public boolean contains(Point p) {
		for (Rectangle rect : rects) {
			if (rect.contains(p)) {
				return true;
			}
		}
		
		return false;
	}
	
}
