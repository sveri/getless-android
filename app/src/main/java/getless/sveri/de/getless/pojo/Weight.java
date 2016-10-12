package getless.sveri.de.getless.pojo;

import java.util.Date;

/**
 * Created by sveri on 21.09.16.
 */

public class Weight {

    private float weight;

    private Date weightedAt;

    public float getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = Math.round(10 * weight) / 10;
    }

    public Date getWeightedAt() {
        return weightedAt;
    }

    public void setWeightedAt(Date weightedAt) {
        this.weightedAt = weightedAt;
    }
}
