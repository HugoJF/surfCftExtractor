package surfCftExtractor;

import configuration.Configuration;
import cftExtractorRecode.CftExtractorRecode;
import surfExtractor.SurfExtractor;
import surfExtractor.exporter.WekaExporter;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class SurfCftExtractor {

	/**
	 * This method is used whe using SurfCftExtractor directly from it's executable
	 * 
	 * @param args Configuration object containing needed parameters 
	 */
	public static void main(String[] args) {
		// Setup parameters
		Configuration config = new Configuration();
		
		config.addNewValidParameter("imageset.path", true);
		config.addNewValidParameter("imageset.relation", true);
		config.addNewValidParameter("arff.path", true);
		config.addNewValidParameter("kmeans.kvalue", true);
		config.addNewValidParameter("kmeans.iteration", true);

		// Read from startup arguments
		config.readFromRunArgs(args);

		// Verify if we don't have a needed argument
		try {
			config.verifyArgs();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Print parameters on screen
		config.debugParameters();

		// Initiate extractors
		SurfExtractor se = new SurfExtractor();
		CftExtractorRecode cer = new CftExtractorRecode();

		// Extract surf attributes
		Instances surfInstances = se.generateInstances(config);
		
		// Extract cft attributes
		Instances cftInstances = cer.generateInstances(config);
		

		// Create the surfInstances exporter
		WekaExporter wieSurf = new WekaExporter(surfInstances);
		
		// Create the cftInstances exporter
		WekaExporter wieCft = new WekaExporter(cftInstances);

		//Set path for cft and surf instances
		wieSurf.setPath(config.getConfiguration("arff.path") + ".cft.arff");
		wieCft.setPath(config.getConfiguration("arff.path") + "surf.arff");
		
		//Export cft and surf instances for debugging
		wieSurf.export();
		wieCft.export();
		
		// Delete class from surf attributes
		surfInstances.deleteAttributeAt(surfInstances.numAttributes() - 1);

		// Merge both attributes instances
		Instances mergedInstances = Instances.mergeInstances(surfInstances, cftInstances);

		// Create the mergedInstance arff exporter
		WekaExporter wie = new WekaExporter(mergedInstances);
		

		// Set final path
		wie.setPath(config.getConfiguration("arff.path"));

		// Export final file;
		wie.export();
	}

	/**
	 * This method is used when SurfCftExtractor is used as a library
	 * 
	 * @param config Configuration object containing needed parameters 
	 */
	public void run(Configuration config) {
		// Verify if we don't have a needed argument
		try {
			config.verifyArgs();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Print parameters on screen
		config.debugParameters();

		// Initiate extractors
		SurfExtractor se = new SurfExtractor();
		CftExtractorRecode cer = new CftExtractorRecode();

		// Extract surf attributes
		Instances surfInstances = se.generateInstances(config);

		// Extract cft attributes
		Instances cftInstances = cer.generateInstances(config);

		// Delete class from surf attributes
		surfInstances.deleteAttributeAt(surfInstances.numAttributes() - 1);

		// Merge both attributes instances
		Instances mergedInstances = Instances.mergeInstances(surfInstances, cftInstances);

		// Instantiate InstanceExporter
		WekaExporter wie = new WekaExporter(mergedInstances);

		// Set final path
		//wie.setPath(arffPath);
		wie.setPath(config.getConfiguration("arff.path"));

		// Export final file;
		wie.export();
	}

}
