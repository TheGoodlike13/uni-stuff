package com.goodlike.interpreter.plan;

import com.goodlike.chaining.ChainingType;
import com.goodlike.interpreter.Rule;

import java.util.Iterator;
import java.util.List;

public interface PlanFinder extends Iterable<Rule> {

    void makePlan(List<Rule> rules);
    Iterator<Rule> iterator();
    boolean isEmpty();

    ChainingType method();

}
