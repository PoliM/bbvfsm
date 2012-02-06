/*******************************************************************************
 *  Copyright 2010, 2011 bbv Software Services AG, Ueli Kurmann
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * Contributors:
 *     bbv Software Services AG (http://www.bbv.ch), Ueli Kurmann
 *******************************************************************************/
package ch.bbv.fsm.acceptance.tennis;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.action.Action;
import ch.bbv.fsm.impl.AbstractStateMachine;
import ch.bbv.fsm.impl.AbstractStateMachineDefinition;

/**
 * Example: Tennis Scorer.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 * 
 */
public class Tennis {

	private class TennisTestStateMachine extends
			AbstractStateMachine<TennisTestStateMachine, States, Events> {

		private States state;

		public States getState() {
			return state;
		}

		protected TennisTestStateMachine(
				final StateMachine<States, Events> driver) {
			super(driver);
		}

		public void setState(final States state) {
			this.state = state;
		}

	}

	private class TennisTestStateMachineDefinition
			extends
			AbstractStateMachineDefinition<TennisTestStateMachine, States, Events> {

		public TennisTestStateMachineDefinition() {
			super("TennisTestStateMachineDefinition", States._0_0);
		}

		@Override
		protected TennisTestStateMachine createStateMachine(
				final StateMachine<States, Events> driver) {
			return new TennisTestStateMachine(driver);
		}

	}

	public enum Events {
		A_Scores, B_Scores
	}

	public enum States {
		_0_0, _0_15, _0_30, _0_40, _15_15, _15_30, _15_40, _30_30, _30_40, _15_0, _30_0, _30_15, _40_0, _40_15, _40_30, _A_GAME, _B_GAME, _DEUCE, _A_ADV, _B_ADV;
	}

	private TennisTestStateMachineDefinition scorer;

	public static class SimpleActionClass implements
			Action<TennisTestStateMachine, States, Events> {

		@Override
		public void execute(final TennisTestStateMachine stateMachine,
				final Object... arguments) {

			stateMachine.setState((States) arguments[0]);
		}

	}

	@Before
	public void setup() {
		this.scorer = new TennisTestStateMachineDefinition();

		this.scorer.in(States._0_0).executeOnEntry(SimpleActionClass.class,
				States._0_0);
		this.scorer.in(States._0_0).on(Events.A_Scores).goTo(States._15_0);
		this.scorer.in(States._0_0).on(Events.B_Scores).goTo(States._0_15);

		this.scorer.in(States._0_15).executeOnEntry(SimpleActionClass.class,
				States._0_15);
		this.scorer.in(States._0_15).on(Events.A_Scores).goTo(States._15_15);
		this.scorer.in(States._0_15).on(Events.B_Scores).goTo(States._0_30);

		this.scorer.in(States._0_30).executeOnEntry(SimpleActionClass.class,
				States._0_30);
		this.scorer.in(States._0_30).on(Events.A_Scores).goTo(States._15_30);
		this.scorer.in(States._0_30).on(Events.B_Scores).goTo(States._0_40);

		this.scorer.in(States._0_40).executeOnEntry(SimpleActionClass.class,
				States._0_40);
		this.scorer.in(States._0_40).on(Events.A_Scores).goTo(States._15_40);
		this.scorer.in(States._0_40).on(Events.B_Scores).goTo(States._B_GAME);

		this.scorer.in(States._15_0).executeOnEntry(SimpleActionClass.class,
				States._15_0);
		this.scorer.in(States._15_0).on(Events.A_Scores).goTo(States._30_0);
		this.scorer.in(States._15_0).on(Events.B_Scores).goTo(States._15_15);

		this.scorer.in(States._15_15).executeOnEntry(SimpleActionClass.class,
				States._15_15);
		this.scorer.in(States._15_15).on(Events.A_Scores).goTo(States._30_15);
		this.scorer.in(States._15_15).on(Events.B_Scores).goTo(States._15_30);

		this.scorer.in(States._15_30).executeOnEntry(SimpleActionClass.class,
				States._15_30);
		this.scorer.in(States._15_30).on(Events.A_Scores).goTo(States._30_30);
		this.scorer.in(States._15_30).on(Events.B_Scores).goTo(States._15_40);

		this.scorer.in(States._15_40).executeOnEntry(SimpleActionClass.class,
				States._15_40);
		this.scorer.in(States._15_40).on(Events.A_Scores).goTo(States._30_40);
		this.scorer.in(States._15_40).on(Events.B_Scores).goTo(States._B_GAME);

		this.scorer.in(States._30_0).executeOnEntry(SimpleActionClass.class,
				States._30_0);
		this.scorer.in(States._30_0).on(Events.A_Scores).goTo(States._40_0);
		this.scorer.in(States._30_0).on(Events.B_Scores).goTo(States._30_15);

		this.scorer.in(States._30_15).executeOnEntry(SimpleActionClass.class,
				States._30_15);
		this.scorer.in(States._30_15).on(Events.A_Scores).goTo(States._40_15);
		this.scorer.in(States._30_15).on(Events.B_Scores).goTo(States._30_30);

		this.scorer.in(States._30_30).executeOnEntry(SimpleActionClass.class,
				States._30_30);
		this.scorer.in(States._30_30).on(Events.A_Scores).goTo(States._40_30);
		this.scorer.in(States._30_30).on(Events.B_Scores).goTo(States._30_40);

		this.scorer.in(States._30_40).executeOnEntry(SimpleActionClass.class,
				States._30_40);
		this.scorer.in(States._30_40).on(Events.A_Scores).goTo(States._DEUCE);
		this.scorer.in(States._30_40).on(Events.B_Scores).goTo(States._B_GAME);

		this.scorer.in(States._40_0).executeOnEntry(SimpleActionClass.class,
				States._40_0);
		this.scorer.in(States._40_0).on(Events.A_Scores).goTo(States._A_GAME);
		this.scorer.in(States._40_0).on(Events.B_Scores).goTo(States._40_15);

		this.scorer.in(States._40_15).executeOnEntry(SimpleActionClass.class,
				States._40_15);
		this.scorer.in(States._40_15).on(Events.A_Scores).goTo(States._A_GAME);
		this.scorer.in(States._40_15).on(Events.B_Scores).goTo(States._40_30);

		this.scorer.in(States._40_30).executeOnEntry(SimpleActionClass.class,
				States._40_30);
		this.scorer.in(States._40_30).on(Events.A_Scores).goTo(States._A_GAME);
		this.scorer.in(States._40_30).on(Events.B_Scores).goTo(States._DEUCE);

		this.scorer.in(States._DEUCE).executeOnEntry(SimpleActionClass.class,
				States._DEUCE);
		this.scorer.in(States._DEUCE).on(Events.A_Scores).goTo(States._A_ADV);
		this.scorer.in(States._DEUCE).on(Events.B_Scores).goTo(States._B_ADV);

		this.scorer.in(States._A_ADV).executeOnEntry(SimpleActionClass.class,
				States._A_ADV);
		this.scorer.in(States._A_ADV).on(Events.A_Scores).goTo(States._A_GAME);
		this.scorer.in(States._A_ADV).on(Events.B_Scores).goTo(States._DEUCE);

		this.scorer.in(States._B_ADV).executeOnEntry(SimpleActionClass.class,
				States._B_ADV);
		this.scorer.in(States._B_ADV).on(Events.A_Scores).goTo(States._DEUCE);
		this.scorer.in(States._B_ADV).on(Events.B_Scores).goTo(States._B_GAME);

		this.scorer.in(States._A_GAME).executeOnEntry(SimpleActionClass.class,
				States._A_GAME);
		this.scorer.in(States._B_GAME).executeOnEntry(SimpleActionClass.class,
				States._B_GAME);
	}

	@Test
	public void scoreWhenIn0to0AandBScores3TimesSwitchingThenDeuce() {
		final TennisTestStateMachine testee = scorer
				.createPassiveStateMachine("Tennis-0");
		testee.start();

		final States initialState = testee.getState();

		testee.fire(Events.A_Scores);
		final States score1 = testee.getState();

		testee.fire(Events.B_Scores);
		final States score2 = testee.getState();

		testee.fire(Events.A_Scores);
		final States score3 = testee.getState();

		testee.fire(Events.B_Scores);
		final States score4 = testee.getState();

		testee.fire(Events.A_Scores);
		final States score5 = testee.getState();

		testee.fire(Events.B_Scores);
		final States score6 = testee.getState();

		testee.terminate();

		Assert.assertEquals(States._0_0, initialState);
		Assert.assertEquals(States._15_0, score1);
		Assert.assertEquals(States._15_15, score2);
		Assert.assertEquals(States._30_15, score3);
		Assert.assertEquals(States._30_30, score4);
		Assert.assertEquals(States._40_30, score5);
		Assert.assertEquals(States._DEUCE, score6);
	}

	@Test
	public void scoreWhenIn0to0AScoresrTimesThenAWins() {
		final TennisTestStateMachine testee = scorer
				.createPassiveStateMachine("Tennis-1");
		testee.start();

		final States initialState = testee.getState();

		testee.fire(Events.A_Scores);
		final States score1 = testee.getState();

		testee.fire(Events.A_Scores);
		final States score2 = testee.getState();

		testee.fire(Events.A_Scores);
		final States score3 = testee.getState();

		testee.fire(Events.A_Scores);
		final States score4 = testee.getState();

		testee.terminate();

		Assert.assertEquals(States._0_0, initialState);
		Assert.assertEquals(States._15_0, score1);
		Assert.assertEquals(States._30_0, score2);
		Assert.assertEquals(States._40_0, score3);
		Assert.assertEquals(States._A_GAME, score4);
	}

	@Test
	public void scoreWhenIn0to0BScoresrTimesThenBWins() {
		final TennisTestStateMachine testee = scorer.createPassiveStateMachine(
				"Tennis-1", States._0_0);
		testee.start();

		final States initialState = testee.getState();

		testee.fire(Events.B_Scores);
		final States score1 = testee.getState();

		testee.fire(Events.B_Scores);
		final States score2 = testee.getState();

		testee.fire(Events.B_Scores);
		final States score3 = testee.getState();

		testee.fire(Events.B_Scores);
		final States score4 = testee.getState();

		Assert.assertEquals(States._0_0, initialState);
		Assert.assertEquals(States._0_15, score1);
		Assert.assertEquals(States._0_30, score2);
		Assert.assertEquals(States._0_40, score3);
		Assert.assertEquals(States._B_GAME, score4);
	}

	@Test
	public void testScorerWhenDeuceAndAScores2TimesThenAWins() {
		final TennisTestStateMachine testee = scorer.createPassiveStateMachine(
				"Tennis-1", States._DEUCE);
		testee.start();

		testee.fire(Events.A_Scores);
		final States score1 = testee.getState();

		testee.fire(Events.A_Scores);
		final States score2 = testee.getCurrentState();

		testee.terminate();

		Assert.assertEquals(States._A_ADV, score1);
		Assert.assertEquals(States._A_GAME, score2);
	}

	@Test
	public void testScorerWhenDeuceAndBfollowedByAScoresThenDeuce() {
		final TennisTestStateMachine testee = scorer.createPassiveStateMachine(
				"Tennis-1", States._DEUCE);
		testee.start();

		testee.fire(Events.A_Scores);
		final States score1 = testee.getState();

		testee.fire(Events.B_Scores);
		final States score2 = testee.getState();

		testee.terminate();

		Assert.assertEquals(States._A_ADV, score1);
		Assert.assertEquals(States._DEUCE, score2);
	}

	@Test
	public void testScorerWhenDeuceAndBScores2TimesThenBWins() {
		final TennisTestStateMachine testee = scorer.createPassiveStateMachine(
				"Tennis-1", States._DEUCE);
		testee.start();

		testee.fire(Events.B_Scores);
		final States score1 = testee.getState();

		testee.fire(Events.B_Scores);
		final States score2 = testee.getState();

		testee.terminate();

		Assert.assertEquals(States._B_ADV, score1);
		Assert.assertEquals(States._B_GAME, score2);
	}

}
