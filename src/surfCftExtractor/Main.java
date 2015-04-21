package surfCftExtractor;

import cftExtractor.cft_extractor.CftExtractor;
import cftExtractor.config.Configuration;
import surfExtractor.SurfExtractor;
import surfExtractor.exporter.WekaExporter;
import surfExtractor.exporter.WekaInstanceExporter;
import weka.core.Instances;

public class Main {

	public static void main(String[] args) {
		SurfExtractor se = new SurfExtractor();
		Instances surfInstances = se.generateInstances("c://polen23e_min", 50, 2, "Test Arff");
		
		
		CftExtractor ce = new CftExtractor();
		Instances cftInstances = ce.getInstances("cftInstances", "c:/polen23e_min");
		
		WekaInstanceExporter wie = new WekaInstanceExporter();
		WekaInstanceExporter wie2 = new WekaInstanceExporter();
		wie.setPath("d://wie_test.arff");
		wie.setInstances(surfInstances);
		wie.export();
		
		wie2.setPath("d://wie_Test2.arff");
		wie2.setInstances(cftInstances);
		wie2.export();
	}

}
