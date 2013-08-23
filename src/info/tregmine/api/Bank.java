package info.tregmine.api;

import info.tregmine.quadtree.Rectangle;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Bank
{
    private int id;
    private String name;

    private String owner;

    private List<Rectangle> rects;
    private HashMap<String, Long> accounts;

    public Bank(String name)
    {
        this.setName(name);
        
        rects = Lists.newArrayList();
        accounts = Maps.newHashMap();
    }
    
    public Bank(int id)
    {
        this.id = id;
        
        rects = Lists.newArrayList();
        accounts = Maps.newHashMap();
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getOwner()
    {
        return owner;
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
    }
    
    public List<Rectangle> getRects()
    {
        return rects;
    }

    public void addRect(Rectangle rect)
    {
        rects.add(rect);
    }

    public void setRects(List<Rectangle> rects)
    {
        this.rects = rects;
    }
    
    public HashMap<String, Long> getAccounts()
    {
        return accounts;
    }
    
    public void setAccounts(HashMap<String, Long> accounts)
    {
        this.accounts = accounts;
    }

}
