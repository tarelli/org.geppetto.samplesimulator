/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2011, 2013 OpenWorm.
 * http://openworm.org
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *
 * Contributors:
 *     	OpenWorm - http://openworm.org/people.html
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/

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
