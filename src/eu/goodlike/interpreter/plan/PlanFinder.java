package eu.goodlike.interpreter.plan;

import eu.goodlike.chaining.ChainingType;
import eu.goodlike.interpreter.Rule;

import java.util.Iterator;
import java.util.List;

public interface PlanFinder extends Iterable<Rule> {

    void makePlan(List<Rule> rules);
    Iterator<Rule> iterator();
    boolean isEmpty();

    ChainingType method();

}
