
package com.uwetrottmann.shopr.algorithm.model;

import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.Arrays;
import java.util.List;

public class Color extends GenericAttribute {

    private static UndirectedGraph<Color.Value, DefaultEdge> sSimilarValues;

    static {
        sSimilarValues = new SimpleGraph<Color.Value, DefaultEdge>(DefaultEdge.class);

        Value[] values = Value.values();
        for (Value value : values) {
            sSimilarValues.addVertex(value);
        }

        /**
         * Stores similar price values in an undirected graph. This is rather
         * subjective, e.g. white is similar to grey. Red is similar to pink,
         * etc.
         */
        sSimilarValues.addEdge(Value.BLUE, Value.PURPLE);
        sSimilarValues.addEdge(Value.BLUE, Value.TURQUOISE);
        sSimilarValues.addEdge(Value.RED, Value.PURPLE);
        sSimilarValues.addEdge(Value.RED, Value.PINK);
        sSimilarValues.addEdge(Value.YELLOW, Value.ORANGE);
        sSimilarValues.addEdge(Value.YELLOW, Value.GOLD);
        sSimilarValues.addEdge(Value.BLACK, Value.GREY);
        sSimilarValues.addEdge(Value.WHITE, Value.BEIGE);
        sSimilarValues.addEdge(Value.WHITE, Value.GREY);
        sSimilarValues.addEdge(Value.WHITE, Value.SILVER);
        sSimilarValues.addEdge(Value.GREY, Value.SILVER);
        sSimilarValues.addEdge(Value.COLORED, Value.MIXED);
        sSimilarValues.addEdge(Value.BROWN, Value.BEIGE);
        sSimilarValues.addEdge(Value.GREEN, Value.PETROL);
        sSimilarValues.addEdge(Value.GREEN, Value.OLIVE);
      
    }

    public static final String ID = "color";

    public enum Value implements AttributeValue {
        BLUE("Blue"),
        RED("Red"),
        PINK("Pink"), // rosa zu Deutsch
        PURPLE("Purple"),
        YELLOW("Yellow"),
        BROWN("Brown"),
        COLORED("Colored"),
        MIXED("Mixed"),
        GREY("Grey"),
        GREEN("Green"),
        ORANGE("Orange"),
        BLACK("Black"),
        TURQUOISE("Turquoise"),
        WHITE("White"),
        BEIGE("Beige"),
        PETROL("Petrol"),
        OLIVE("Olive"),
        GOLD("Gold"),
        SILVER("Silver"),
        UNKNOWN("Unknown");

        String mDescriptor;

        Value(String name) {
            mDescriptor = name;
        }

        @Override
        public String descriptor() {
            return mDescriptor;
        }

        @Override
        public int index() {
            return ordinal();
        }
    }

    public Color() {
        int numValues = Value.values().length;
        mValueWeights = new double[numValues];
        Arrays.fill(mValueWeights, 1.0 / numValues);
    }

    public Color(Value value) {
        setWeights(value);
    }

    /**
     * Tries to match the given string with a {@link Color.Value}.
     */
    public Color(String value) {
        if ("Blau".equals(value)) {
            setWeights(Color.Value.BLUE);
        }
        else if ("Braun".equals(value)) {
            setWeights(Color.Value.BROWN);
        }
        else if ("Bunt".equals(value)) {
            setWeights(Color.Value.COLORED);
        }
        else if ("Gelb".equals(value)) {
            setWeights(Color.Value.YELLOW);
        }
        else if ("Gemischt".equals(value)) {
            setWeights(Color.Value.MIXED);
        }
        else if ("Grau".equals(value)) {
            setWeights(Color.Value.GREY);
        }
        else if ("GrÃ¼n".equals(value) || "Grün".equals(value)) {
            setWeights(Color.Value.GREEN);
        }
        else if ("Orange".equals(value)) {
            setWeights(Color.Value.ORANGE);
        }
        else if ("Rosa".equals(value)) {
            setWeights(Color.Value.PINK);
        }
        else if ("Rot".equals(value)) {
            setWeights(Color.Value.RED);
        }
        else if ("Schwarz".equals(value)) {
            setWeights(Color.Value.BLACK);
        }
        else if ("TÃ¼rkis".equals(value) || "Türkis".equals(value)) {
            setWeights(Color.Value.TURQUOISE);
        }
        else if ("Violett".equals(value)) {
            setWeights(Color.Value.PURPLE);
        }
        else if ("WeiÃŸ".equals(value) || "Weiß".equals(value)) {
            setWeights(Color.Value.WHITE);
        }
        else if ("Beige".equals(value)) {
            setWeights(Color.Value.BEIGE);
        }
        else if ("Petrol".equals(value)) {
            setWeights(Color.Value.PETROL);
        }
        else if ("Oliv".equals(value)) {
            setWeights(Color.Value.OLIVE);
        }
        else if ("Gold".equals(value)) {
            setWeights(Color.Value.GOLD);
        }
        else if ("Silber".equals(value)) {
            setWeights(Color.Value.SILVER);
        }
        else {
        	setWeights(Color.Value.UNKNOWN);
        }
    }

    private void setWeights(Value value) {
        mValueWeights = new double[Value.values().length];
        Arrays.fill(mValueWeights, 0.0);
        mValueWeights[value.ordinal()] = 1.0;
        currentValue(value);
    }

    @Override
    public String id() {
        return ID;
    }

    @Override
    public AttributeValue[] getValueSymbols() {
        return Value.values();
    }

    @Override
    public void likeValue(int indexLiked, double[] weights) {
        Value[] values = Value.values();
        Value valueLiked = values[indexLiked];
        List<Value> similarValues = Graphs.neighborListOf(sSimilarValues, valueLiked);

        // do regular like for liked value
        super.likeValue(indexLiked, weights);

        if (similarValues.isEmpty()) {
            // no similars: done!
            return;
        }

        // now do dampened like on similar values
        double increaseLiked = 1.0 / (weights.length - 1);
        double increaseSimilars = increaseLiked / 2;
        // per similar value increase
        double increaseSimilar = increaseSimilars / similarValues.size();
        // per non-similar and non-liked value decrease
        double decreaseOthers = increaseSimilars / (weights.length - similarValues.size() - 1);

        // actually add and subtract
        for (int i = 0; i < weights.length; i++) {
            if (i == indexLiked) {
                // skip liked value
                continue;
            }
            if (hasValueWithSameIndex(similarValues, i)) {
                // increase similar values
                weights[i] += increaseSimilar;
            } else {
                // decrease other values
                weights[i] -= decreaseOthers;
                // floor at 0.0
                if (weights[i] < 0) {
                    weights[i] = 0.0;
                }
            }
        }

        ensureSumBound(weights);
    }

    /**
     * Checks whether one of the values has the given index.
     */
    private boolean hasValueWithSameIndex(List<Value> values, int index) {
        for (Value value : values) {
            if (value.index() == index) {
                return true;
            }
        }
        return false;
    }
}
