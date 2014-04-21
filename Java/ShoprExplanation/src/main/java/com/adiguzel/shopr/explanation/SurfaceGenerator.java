package com.adiguzel.shopr.explanation;

import com.adiguzel.shopr.explanation.model.Explanation;

public interface SurfaceGenerator {
	
	public CharSequence transform(Explanation abstractExplanation);

}
