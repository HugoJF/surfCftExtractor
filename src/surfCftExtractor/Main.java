package surfCftExtractor;

import configuration.Configuration;
import cftExtractorRecode.CftExtractorRecode;
import surfExtractor.SurfExtractor;
import surfExtractor.exporter.WekaExporter;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class Main {

	public static void main(String[] args) {
		Configuration.addNewValidParameter("imageset.path", true);
		Configuration.addNewValidParameter("imageset.relation", true);
		Configuration.addNewValidParameter("arff.path", true);
		Configuration.addNewValidParameter("kmeans.kvalue", true);
		Configuration.addNewValidParameter("kmeans.iteration", true);
		
		Configuration.readFromRunArgs(args);
		
		try {
			Configuration.verifyArgs();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Configuration.debugParameters();
		
		SurfExtractor se = new SurfExtractor();
		CftExtractorRecode cer = new CftExtractorRecode();
		
		Instances surfInstances = se.generateInstances(Configuration.getConfiguration("imageset.path"), 
				Integer.valueOf(Configuration.getConfiguration("kmeans.kvalue")), 
				Integer.valueOf(Configuration.getConfiguration("kmeans.iteration")), 
				Configuration.getConfiguration("imageset.relation"));
		
		Instances cftInstances = cer.generateInstances(Configuration.getConfiguration("imageset.relation"), Configuration.getConfiguration("imageset.path"));
		
		surfInstances.deleteAttributeAt(surfInstances.numAttributes() - 1);
		
		Instances mergedInstances = Instances.mergeInstances(surfInstances, cftInstances);
		
		WekaExporter wie = new WekaExporter(mergedInstances);
		
		wie.setPath("d://wie_test.arff");
		wie.export();
		
		Configuration.debugParameters();
	}

}
