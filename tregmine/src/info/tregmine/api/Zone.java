package info.tregmine.api;

import java.util.HashSet;
import java.util.Set;

import info.tregmine.quadtree.Point;
import info.tregmine.quadtree.Rectangle;

public class Zone 
{
	public enum Permission
	{
		FullRights,
		CanEnterNoBuild,
		NoEnterNoBuild;
	}
	
	private int id;
	private String name;
	private String owner;
	private Rectangle rect;
	
	private String textEnter;
	private String textExit;
	
	private Set<String> builders;
	private Set<String> banned;
	
	public Zone()
	{
		builders = new HashSet<String>();
		banned = new HashSet<String>();
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

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Rectangle getRect() {
		return rect;
	}

	public void setRect(Rectangle rect) {
		this.rect = rect;
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
	
	public boolean contains(Point p)
	{
		return rect.contains(p);
	}
	
	public void addBuilder(String name)
	{
		builders.add(name);
	}
	
	public boolean isBuilder(String name)
	{
		return builders.contains(name);
	}
	
	public void addBan(String name)
	{
		banned.add(name);
	}
	
	public boolean isBanned(String name)
	{
		return banned.contains(name);
	}
}
