package surfCftExtractor;

import configuration.Configuration;
import cftExtractorRecode.CftExtractorRecode;
import surfExtractor.SurfExtractor;
import surfExtractor.exporter.WekaExporter;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class SurfCftExtractor {

	public static void main(String[] args) {
		// Setup parameters
		Configuration.addNewValidParameter("imageset.path", true);
		Configuration.addNewValidParameter("imageset.relation", true);
		Configuration.addNewValidParameter("arff.path", true);
		Configuration.addNewValidParameter("kmeans.kvalue", true);
		Configuration.addNewValidParameter("kmeans.iteration", true);

		// Read from startup arguments
		Configuration.readFromRunArgs(args);

		// Verify if we don't have a needed argument
		try {
			Configuration.verifyArgs();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Print parameters on screen
		Configuration.debugParameters();

		// Initiate extractors
		SurfExtractor se = new SurfExtractor();
		CftExtractorRecode cer = new CftExtractorRecode();

		// Extract surf attributes
		Instances surfInstances = se.generateInstances(Configuration.getConfiguration("imageset.path"), Integer.valueOf(Configuration.getConfiguration("kmeans.kvalue")), Integer.valueOf(Configuration.getConfiguration("kmeans.iteration")), Configuration.getConfiguration("imageset.relation"));

		// Extract cft attributes
		Instances cftInstances = cer.generateInstances(Configuration.getConfiguration("imageset.relation"), Configuration.getConfiguration("imageset.path"));

		// Delete class from surf attributes
		surfInstances.deleteAttributeAt(surfInstances.numAttributes() - 1);

		// Merge both attributes instances
		Instances mergedInstances = Instances.mergeInstances(surfInstances, cftInstances);

		// Instantiate InstanceExporter
		WekaExporter wie = new WekaExporter(mergedInstances);

		// Set final path
		wie.setPath(Configuration.getConfiguration("arff.path"));

		// Export final file;
		wie.export();
	}

	public void run(String imageSetPath, String imageSetRelation, String arffPath, int kValue, int iterations) {
		// Verify if we don't have a needed argument
		try {
			Configuration.verifyArgs();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Print parameters on screen
		Configuration.debugParameters();

		// Initiate extractors
		SurfExtractor se = new SurfExtractor();
		CftExtractorRecode cer = new CftExtractorRecode();

		// Extract surf attributes
		Instances surfInstances = se.generateInstances(imageSetPath, kValue, iterations, imageSetRelation);

		// Extract cft attributes
		Instances cftInstances = cer.generateInstances(imageSetRelation, imageSetPath);

		// Delete class from surf attributes
		surfInstances.deleteAttributeAt(surfInstances.numAttributes() - 1);

		// Merge both attributes instances
		Instances mergedInstances = Instances.mergeInstances(surfInstances, cftInstances);

		// Instantiate InstanceExporter
		WekaExporter wie = new WekaExporter(mergedInstances);

		// Set final path
		wie.setPath(arffPath);

		// Export final file;
		wie.export();
	}

}
