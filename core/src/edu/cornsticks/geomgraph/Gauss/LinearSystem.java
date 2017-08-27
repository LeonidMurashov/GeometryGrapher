package edu.cornsticks.geomgraph.Gauss;

import java.util.ArrayList;
import java.util.List;

class LinearSystem<N extends Number, T extends Gauss<N, T>> {
    private List<T> list = new ArrayList<T>();

    T get(int index){
        return list.get(index);
    }

    void push(T elem){
        list.add(elem);
    }

    public int size(){
        return list.size();
    }

    N itemAt(int i, int j){
        return list.get(i).at(j);
    }
}
