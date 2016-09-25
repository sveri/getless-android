package getless.sveri.de.getless.pojo;

import java.util.Date;

/**
 * Created by sveri on 21.09.16.
 */

public class Weight {

    private int weight;

    private Date weightedAt;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Date getWeightedAt() {
        return weightedAt;
    }

    public void setWeightedAt(Date weightedAt) {
        this.weightedAt = weightedAt;
    }
}
