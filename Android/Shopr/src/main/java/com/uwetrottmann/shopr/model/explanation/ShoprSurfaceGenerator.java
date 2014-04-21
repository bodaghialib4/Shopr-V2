package com.uwetrottmann.shopr.model.explanation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.content.Context;
import android.text.Html;

import com.adiguzel.shopr.explanation.SurfaceGenerator;
import com.adiguzel.shopr.explanation.model.Argument.Type;
import com.adiguzel.shopr.explanation.model.DimensionArgument;
import com.adiguzel.shopr.explanation.model.Explanation;
import com.adiguzel.shopr.explanation.model.LocationContext;
import com.uwetrottmann.shopr.R;

public class ShoprSurfaceGenerator implements SurfaceGenerator {

	private List<String> strongArgumentTemplates, weakArgumentTemplates,
			serendipitousityTemplates, contextArgumentTemplates;
	private String supportingArgumentTemplate;
	private String goodAverageTemplate;
	private Context context;

	public ShoprSurfaceGenerator(Context context) {
		this.context = context;
		initStrongArgumentTemplates(context);
		initWeakArgumentTemplates(context);
		initContextArgumentTemplates(context);
		initSerendipitousityTemplates(context);
		supportingArgumentTemplate = context
				.getString(R.string.explanation_template_on_dimension_weak_supporting_1);
		goodAverageTemplate = context.getString(R.string.explanation_template_average_item);
	}

	public CharSequence transform(Explanation explanation) {
		String explanationText = "";

		if (explanation.hasPrimaryArguments()
				&& !explanation.hasSupportingArguments()) {
			String template = chooseRandomTemplate(strongArgumentTemplates);
			explanationText += String.format(template,
					textify(explanation.primaryArguments()));
		} else if (explanation.hasPrimaryArguments()
				&& explanation.hasSupportingArguments()) {
			String template = chooseRandomTemplate(weakArgumentTemplates);
			String primaryPart = String.format(template,
					textify(explanation.primaryArguments()));
			String supportingPart = String.format(supportingArgumentTemplate,
					textify(explanation.supportingArguments()));

			explanationText += primaryPart + " " + supportingPart;
		}
		else if(explanation.hasSupportingArguments()) {
			DimensionArgument arg = explanation.supportingArguments().iterator().next();
			
			if(arg.type() == Type.SERENDIPITOUS) {
				explanationText += chooseRandomTemplate(serendipitousityTemplates);
			}
			else if(arg.type() == Type.GOOD_AVERAGE) {
				explanationText += goodAverageTemplate;	
			}
		}
		if (explanation.hasContextArguments()) {
			String template = chooseRandomTemplate(contextArgumentTemplates);
			LocationContext locContext = (LocationContext) explanation
					.contextArguments().iterator().next().context();
			explanationText += String.format(template, locContext.distanceToUserInMeters(explanation.item()));
		}

		return Html.fromHtml(explanationText);
	}

	private String textify(Collection<DimensionArgument> arguments) {
		Iterator<DimensionArgument> iterator = arguments.iterator();

		List<String> argumentValues = new ArrayList<String>();
		while (iterator.hasNext()) {
			DimensionArgument arg = iterator.next();
			argumentValues.add(arg.dimension().attribute().getCurrentValue()
					.descriptor().toLowerCase(Locale.ENGLISH));
		}
		return textify(argumentValues);
	}
	


	private String textify(List<String> elements) {
		String and = context.getString(R.string.conjunction_and);

		if (elements.size() == 0) {
			return "";
		} else if (elements.size() == 1) {
			return bold(elements.get(0));
		} else if (elements.size() == 2) {
			String first = elements.get(0);
			String second = elements.get(1);
			return bold(first) + and + bold(second);
		} else {
			String last = elements.get(elements.size() - 1);
			List<String> headList = elements.subList(0, elements.size() - 1);
			return commaSeperated(headList) + and + bold(last);
		}
	}

	private String commaSeperated(List<String> elements) {
		String comma = context.getString(R.string.conjunction_comma);

		if (elements.size() == 0) {
			return "";
		} else if (elements.size() == 1) {
			return elements.get(0);
		} else if (elements.size() == 2) {
			String first = elements.get(0);
			String second = elements.get(1);
			return bold(first) + comma + bold(second);
		} else {
			String last = elements.get(elements.size() - 1);
			List<String> headList = elements.subList(0, elements.size() - 1);
			return commaSeperated(headList) + comma + bold(last);
		}
	}
	
	private String bold(String text) {
		return "<b>" + text + "</b>";
	}

	private String chooseRandomTemplate(List<String> templates) {
		return templates.get(new Random().nextInt(templates.size()));
	}

	private List<String> initStrongArgumentTemplates(Context context) {
		strongArgumentTemplates = new ArrayList<String>();
		strongArgumentTemplates
				.add(context
						.getString(R.string.explanation_template_on_dimension_strong_1));
		strongArgumentTemplates
				.add(context
						.getString(R.string.explanation_template_on_dimension_strong_2));
		return strongArgumentTemplates;
	}

	private List<String> initWeakArgumentTemplates(Context context) {
		weakArgumentTemplates = new ArrayList<String>();
		weakArgumentTemplates.add(context
				.getString(R.string.explanation_template_on_dimension_weak_1));
		weakArgumentTemplates.add(context
				.getString(R.string.explanation_template_on_dimension_weak_2));
		return weakArgumentTemplates;
	}

	private List<String> initContextArgumentTemplates(Context context) {
		contextArgumentTemplates = new ArrayList<String>();
		contextArgumentTemplates.add(context
				.getString(R.string.explanation_template_context_location));
		contextArgumentTemplates
				.add(context
						.getString(R.string.explanation_template_context_location_average));
		return contextArgumentTemplates;
	}

	private List<String> initSerendipitousityTemplates(Context context) {
		serendipitousityTemplates = new ArrayList<String>();
		serendipitousityTemplates.add(context
				.getString(R.string.explanation_template_serendipidity_1));
		serendipitousityTemplates.add(context
				.getString(R.string.explanation_template_serendipidity_2));
		serendipitousityTemplates.add(context
				.getString(R.string.explanation_template_serendipidity_3));
		return serendipitousityTemplates;
	}
}
