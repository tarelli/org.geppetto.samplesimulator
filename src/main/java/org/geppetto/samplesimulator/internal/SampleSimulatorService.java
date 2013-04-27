package org.geppetto.samplesimulator.internal;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geppetto.core.model.IModel;
import org.geppetto.core.simulation.ITimeConfiguration;
import org.geppetto.core.simulator.AParallelSimulator;
import org.geppetto.core.solver.ISolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * An simple implementation of the simulator service interface. This
 * implementation is internal to this module and is not exported to other
 * bundles.
 */
@Service
public class SampleSimulatorService extends AParallelSimulator {

	private static Log logger = LogFactory.getLog(SampleSimulatorService.class);

	@Autowired
	private ISolver sampleSolverService;

	private ITimeConfiguration _timeConfiguration;

	/* (non-Javadoc)
	 * @see org.geppetto.core.simulator.ISimulator#simulate(org.geppetto.core.model.IModel)
	 */
	@Override
	public void simulate(IModel model, ITimeConfiguration timeConfiguration) {
		enqueue(model, sampleSolverService);
		_timeConfiguration=timeConfiguration;

	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.simulator.ISimulator#startSimulatorCycle()
	 */
	@Override
	public void startSimulatorCycle() 
	{
		logger.debug("cycle started");
		clearQueue();		
	}

	/* (non-Javadoc)
	 * @see org.geppetto.core.simulator.ISimulator#endSimulaorCycle()
	 */
	@Override
	public void endSimulatorCycle() 
	{
		logger.debug("cycle finished");
		List<List<IModel>> results=sampleSolverService.solve(getQueue(sampleSolverService), _timeConfiguration);
		for(List<IModel> models:results)
		{
			getListener().resultReady(models);
		}		
	}





}
