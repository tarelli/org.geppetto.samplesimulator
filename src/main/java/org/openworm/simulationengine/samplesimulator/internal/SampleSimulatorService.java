package org.openworm.simulationengine.samplesimulator.internal;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openworm.simulationengine.core.model.IModel;
import org.openworm.simulationengine.core.simulation.ITimeConfiguration;
import org.openworm.simulationengine.core.simulator.AParallelSimulator;
import org.openworm.simulationengine.core.solver.ISolver;
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
	 * @see org.openworm.simulationengine.core.simulator.ISimulator#simulate(org.openworm.simulationengine.core.model.IModel)
	 */
	@Override
	public void simulate(IModel model, ITimeConfiguration timeConfiguration) {
		enqueue(model, sampleSolverService);
		_timeConfiguration=timeConfiguration;

	}

	/* (non-Javadoc)
	 * @see org.openworm.simulationengine.core.simulator.ISimulator#startSimulatorCycle()
	 */
	@Override
	public void startSimulatorCycle() 
	{
		logger.debug("cycle started");
		clearQueue();		
	}

	/* (non-Javadoc)
	 * @see org.openworm.simulationengine.core.simulator.ISimulator#endSimulaorCycle()
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
