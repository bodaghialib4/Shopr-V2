package com.uwetrottmann.shopr.algorithm.model;

import java.util.Arrays;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;

public class ClothingType extends GenericAttribute {

	private static UndirectedGraph<ClothingType.Value, DefaultEdge> sSimilarValues;

	static {
		sSimilarValues = new SimpleGraph<ClothingType.Value, DefaultEdge>(
				DefaultEdge.class);

		Value[] values = Value.values();
		for (Value value : values) {
			sSimilarValues.addVertex(value);
		}

		/**
		 * Store similar clothing type values in an undirected graph.
		 */
		sSimilarValues.addEdge(Value.SHIRT, Value.POLOSHIRT);
		sSimilarValues.addEdge(Value.SHIRT, Value.BLOUSE);
		sSimilarValues.addEdge(Value.TROUSERS, Value.JEANS);
		sSimilarValues.addEdge(Value.TROUSERS, Value.SHORTS);
		sSimilarValues.addEdge(Value.TROUSERS, Value.SKIRT);
		sSimilarValues.addEdge(Value.SKIRT, Value.SHORTS);
		sSimilarValues.addEdge(Value.CARDIGAN, Value.SWEATER);
		sSimilarValues.addEdge(Value.TOP, Value.SHIRT);
		sSimilarValues.addEdge(Value.TOP, Value.BLOUSE);
		sSimilarValues.addEdge(Value.TOP, Value.TSHIRT);
		sSimilarValues.addEdge(Value.TSHIRT, Value.SHIRT);
		sSimilarValues.addEdge(Value.TSHIRT, Value.BLOUSE);
		sSimilarValues.addEdge(Value.COAT, Value.JACKET);
		sSimilarValues.addEdge(Value.SWIMSUIT, Value.TRUNKS);
		sSimilarValues.addEdge(Value.SWIMSUIT, Value.BIKINI);
		sSimilarValues.addEdge(Value.LONGSLEEVE, Value.SWEATSHIRT);
	}

	public static final String ID = "clothing-type";

	public enum Value implements AttributeValue {
		UNKNOWN("Unknown", "unknown", Colors.BLACK), 
		SWIMSUIT("Swim suit", "swimsuit", Colors.GOLD), 
		TRUNKS("Trunks", "trunks", Colors.BROWN), 
		BLOUSE("Blouse", "blouse", Colors.OLIVE), 
		SHIRT("Shirt", "shirt", Colors.GREEN), 
		TROUSERS("Trousers", "trousers", Colors.GREY), 
		JEANS("Jeans", "jeans", Colors.BLUE), 
		DRESS("Dress", "dress", Colors.PINK), 
		POLOSHIRT("Poloshirt", "poloshirt", Colors.TURQUOISE), 
		SWEATER("Sweater", "sweater", Colors.SILVER), // Pullover
		SKIRT("Skirt", "skirt", Colors.ORANGE), 
		SHORTS("Shorts", "shorts", Colors.YELLOW), 
		CARDIGAN("Cardigan", "cardigan",  Colors.PURPLE), // Strickjacke
		TOP("Top", "top", Colors.WHITE), 
		TSHIRT("T-Shirt", "tshirt", Colors.RED), 
		SWEATSHIRT("Sweatshirt", "sweatshirt", Colors.PETROL), 
		LONGSLEEVE("Longsleeve", "longsleeve", Colors.BEIGE), 
		BIKINI("Bikini", "bikini", Colors.RODEO_DUST), 
		COAT("Coat", "coat", Colors.PEACH), 
		JACKET("Jacket", "jacket", Colors.AZURE_BLUE);

		private String mDescriptor;
		private String mColor;
        private String mSimpleName;
        private String simpleNamePrefix = "clothing_";

        Value(String name, String simpleName, String color) {
            mDescriptor = name;
            mSimpleName = simpleNamePrefix + simpleName;
            mColor = color;
        }

		@Override
		public String descriptor() {
			return mDescriptor;
		}
		
        @Override
        public String simpleName() {
            return mSimpleName;
        }

		@Override
		public int index() {
			return ordinal();
		}

		@Override
		public String color() {
			return mColor;
		}
	}

	public ClothingType() {
		int numValues = Value.values().length;
		mValueWeights = new double[numValues];
		Arrays.fill(mValueWeights, 1.0 / numValues);
	}

	public ClothingType(Value value) {
		setWeights(value);
	}

	public ClothingType(String name) {
		if ("Badeanzug".equals(name)) {
			setWeights(Value.SWIMSUIT);
		} else if ("Badehose".equals(name)) {
			setWeights(Value.TRUNKS);
		} else if ("Bluse".equals(name)) {
			setWeights(Value.BLOUSE);
		} else if ("Hemd".equals(name)) {
			setWeights(Value.SHIRT);
		} else if ("Hose".equals(name)) {
			setWeights(Value.TROUSERS);
		} else if ("Jeans".equals(name)) {
			setWeights(Value.JEANS);
		} else if ("Kleid".equals(name)) {
			setWeights(Value.DRESS);
		} else if ("Poloshirt".equals(name)) {
			setWeights(Value.POLOSHIRT);
		} else if ("Pullover".equals(name)) {
			setWeights(Value.SWEATER);
		} else if ("Rock".equals(name)) {
			setWeights(Value.SKIRT);
		} else if ("Strickjacke".equals(name)) {
			setWeights(Value.CARDIGAN);
		} else if ("Top".equals(name)) {
			setWeights(Value.TOP);
		} else if ("Shorts".equals(name)) {
			setWeights(Value.SHORTS);
		} else if ("T-Shirt".equals(name)) {
			setWeights(Value.TSHIRT);
		} else if ("Sweatshirt".equals(name)) {
			setWeights(Value.SWEATSHIRT);
		} else if ("Sweatshirt".equals(name)) {
			setWeights(Value.SWEATSHIRT);
		} else if ("Langarmshirt".equals(name)) {
			setWeights(Value.LONGSLEEVE);
		} else if ("Bikini".equals(name)) {
			setWeights(Value.BIKINI);
		} else if ("Mantel".equals(name)) {
			setWeights(Value.COAT);
		} else if ("Jacke".equals(name)) {
			setWeights(Value.JACKET);
		} else {
			setWeights(Value.UNKNOWN);
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
	public Value[] getValueSymbols() {
		return Value.values();
	}

	@Override
	protected void likeValue(int indexLiked, double[] weights) {
		Value[] values = Value.values();
		Value valueLiked = values[indexLiked];
		List<Value> similarValues = Graphs.neighborListOf(sSimilarValues,
				valueLiked);

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
		double decreaseOthers = increaseSimilars
				/ (weights.length - similarValues.size() - 1);

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

	@Override
	public AttributeValue[] getAttributeValues() {
		return Value.values();
	}

}
