package org.yekeqi.application;

/**
 * Created by yekeqi on 2015/9/26.
 */
public interface ObservedSubject {
    public void attach(Observer observer);
    public void detach(Observer observer);
    public void notice();
}
