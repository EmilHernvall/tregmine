package info.tregmine.bank;

import info.tregmine.api.bank.Banker;

public class BankerReturnRunnable implements Runnable {

    private Banker entity;

    public BankerReturnRunnable(Banker banker)
    {
        this.entity = banker;
    }

    @Override
    public void run() {
        entity.getBanker().getActions().moveTo(entity.getLocation());
    }
}