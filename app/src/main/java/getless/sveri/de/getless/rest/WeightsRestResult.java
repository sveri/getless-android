package getless.sveri.de.getless.rest;

import java.util.List;

import getless.sveri.de.getless.pojo.Weight;

/**
 * Created by sveri on 21.09.16.
 */

public class WeightsRestResult extends RestResult {

    public List<Weight> getWeights() {
        return weights;
    }

    public void setWeights(List<Weight> weights) {
        this.weights = weights;
    }

    private List<Weight> weights;


}
