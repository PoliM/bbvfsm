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
package ch.bbv.fsm.example;

import static ch.bbv.fsm.impl.Tool.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.PassiveStateMachine;

/**
 * Example: Tennis Scorer.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 * 
 */
public class Tennis {

    public enum Events {
        A_Scores, B_Scores
    }

    public enum States {
        _0_0, _0_15, _0_30, _0_40, _15_15, _15_30, _15_40, _30_30, _30_40, _15_0, _30_0, _30_15, _40_0, _40_15, _40_30, _A_GAME, _B_GAME, _DEUCE, _A_ADV, _B_ADV;
    }

    private StateMachine<States, Events> scorer;

    private States currentState;

    public States getCurrentState() {
        return this.currentState;
    }

    @Test
    public void scoreWhenIn_0_0_A_And_BScores3TimesSwitchingThen_Deuce() {
        this.scorer.initialize(States._0_0);
        this.scorer.start();

        final States initialState = getCurrentState();

        this.scorer.fire(Events.A_Scores);
        final States score1 = getCurrentState();

        this.scorer.fire(Events.B_Scores);
        final States score2 = getCurrentState();

        this.scorer.fire(Events.A_Scores);
        final States score3 = getCurrentState();

        this.scorer.fire(Events.B_Scores);
        final States score4 = getCurrentState();

        this.scorer.fire(Events.A_Scores);
        final States score5 = getCurrentState();

        this.scorer.fire(Events.B_Scores);
        final States score6 = getCurrentState();

        Assert.assertEquals(States._0_0, initialState);
        Assert.assertEquals(States._15_0, score1);
        Assert.assertEquals(States._15_15, score2);
        Assert.assertEquals(States._30_15, score3);
        Assert.assertEquals(States._30_30, score4);
        Assert.assertEquals(States._40_30, score5);
        Assert.assertEquals(States._DEUCE, score6);
    }

    @Test
    public void scoreWhenIn_0_0_AScoresrTimesThen_A_Wins() {
        this.scorer.initialize(States._0_0);
        this.scorer.start();

        final States initialState = getCurrentState();

        this.scorer.fire(Events.A_Scores);
        final States score1 = getCurrentState();

        this.scorer.fire(Events.A_Scores);
        final States score2 = getCurrentState();

        this.scorer.fire(Events.A_Scores);
        final States score3 = getCurrentState();

        this.scorer.fire(Events.A_Scores);
        final States score4 = getCurrentState();

        Assert.assertEquals(States._0_0, initialState);
        Assert.assertEquals(States._15_0, score1);
        Assert.assertEquals(States._30_0, score2);
        Assert.assertEquals(States._40_0, score3);
        Assert.assertEquals(States._A_GAME, score4);
    }

    @Test
    public void scoreWhenIn_0_0_BScoresrTimesThen_B_Wins() {
        this.scorer.initialize(States._0_0);
        this.scorer.start();

        final States initialState = getCurrentState();

        this.scorer.fire(Events.B_Scores);
        final States score1 = getCurrentState();

        this.scorer.fire(Events.B_Scores);
        final States score2 = getCurrentState();

        this.scorer.fire(Events.B_Scores);
        final States score3 = getCurrentState();

        this.scorer.fire(Events.B_Scores);
        final States score4 = getCurrentState();

        Assert.assertEquals(States._0_0, initialState);
        Assert.assertEquals(States._0_15, score1);
        Assert.assertEquals(States._0_30, score2);
        Assert.assertEquals(States._0_40, score3);
        Assert.assertEquals(States._B_GAME, score4);
    }

    public Void setCurrentState(final States state) {
        this.currentState = state;
        return null;
    }

    @Before
    public void setup() {
        this.scorer = new PassiveStateMachine<States, Events>();

        this.scorer.in(States._0_0).executeOnEntry(from(this).setCurrentState(States._0_0));
        this.scorer.in(States._0_0).on(Events.A_Scores).goTo(States._15_0);
        this.scorer.in(States._0_0).on(Events.B_Scores).goTo(States._0_15);

        this.scorer.in(States._0_15).executeOnEntry(from(this).setCurrentState(States._0_15));
        this.scorer.in(States._0_15).on(Events.A_Scores).goTo(States._15_15);
        this.scorer.in(States._0_15).on(Events.B_Scores).goTo(States._0_30);

        this.scorer.in(States._0_30).executeOnEntry(from(this).setCurrentState(States._0_30));
        this.scorer.in(States._0_30).on(Events.A_Scores).goTo(States._15_30);
        this.scorer.in(States._0_30).on(Events.B_Scores).goTo(States._0_40);

        this.scorer.in(States._0_40).executeOnEntry(from(this).setCurrentState(States._0_40));
        this.scorer.in(States._0_40).on(Events.A_Scores).goTo(States._15_40);
        this.scorer.in(States._0_40).on(Events.B_Scores).goTo(States._B_GAME);

        this.scorer.in(States._15_0).executeOnEntry(from(this).setCurrentState(States._15_0));
        this.scorer.in(States._15_0).on(Events.A_Scores).goTo(States._30_0);
        this.scorer.in(States._15_0).on(Events.B_Scores).goTo(States._15_15);

        this.scorer.in(States._15_15).executeOnEntry(from(this).setCurrentState(States._15_15));
        this.scorer.in(States._15_15).on(Events.A_Scores).goTo(States._30_15);
        this.scorer.in(States._15_15).on(Events.B_Scores).goTo(States._15_30);

        this.scorer.in(States._15_30).executeOnEntry(from(this).setCurrentState(States._15_30));
        this.scorer.in(States._15_30).on(Events.A_Scores).goTo(States._30_30);
        this.scorer.in(States._15_30).on(Events.B_Scores).goTo(States._15_40);

        this.scorer.in(States._15_40).executeOnEntry(from(this).setCurrentState(States._15_40));
        this.scorer.in(States._15_40).on(Events.A_Scores).goTo(States._30_40);
        this.scorer.in(States._15_40).on(Events.B_Scores).goTo(States._B_GAME);

        this.scorer.in(States._30_0).executeOnEntry(from(this).setCurrentState(States._30_0));
        this.scorer.in(States._30_0).on(Events.A_Scores).goTo(States._40_0);
        this.scorer.in(States._30_0).on(Events.B_Scores).goTo(States._30_15);

        this.scorer.in(States._30_15).executeOnEntry(from(this).setCurrentState(States._30_15));
        this.scorer.in(States._30_15).on(Events.A_Scores).goTo(States._40_15);
        this.scorer.in(States._30_15).on(Events.B_Scores).goTo(States._30_30);

        this.scorer.in(States._30_30).executeOnEntry(from(this).setCurrentState(States._30_30));
        this.scorer.in(States._30_30).on(Events.A_Scores).goTo(States._40_30);
        this.scorer.in(States._30_30).on(Events.B_Scores).goTo(States._30_40);

        this.scorer.in(States._30_40).executeOnEntry(from(this).setCurrentState(States._30_40));
        this.scorer.in(States._30_40).on(Events.A_Scores).goTo(States._DEUCE);
        this.scorer.in(States._30_40).on(Events.B_Scores).goTo(States._B_GAME);

        this.scorer.in(States._40_0).executeOnEntry(from(this).setCurrentState(States._40_0));
        this.scorer.in(States._40_0).on(Events.A_Scores).goTo(States._A_GAME);
        this.scorer.in(States._40_0).on(Events.B_Scores).goTo(States._40_15);

        this.scorer.in(States._40_15).executeOnEntry(from(this).setCurrentState(States._40_15));
        this.scorer.in(States._40_15).on(Events.A_Scores).goTo(States._A_GAME);
        this.scorer.in(States._40_15).on(Events.B_Scores).goTo(States._40_30);

        this.scorer.in(States._40_30).executeOnEntry(from(this).setCurrentState(States._40_30));
        this.scorer.in(States._40_30).on(Events.A_Scores).goTo(States._A_GAME);
        this.scorer.in(States._40_30).on(Events.B_Scores).goTo(States._DEUCE);

        this.scorer.in(States._DEUCE).executeOnEntry(from(this).setCurrentState(States._DEUCE));
        this.scorer.in(States._DEUCE).on(Events.A_Scores).goTo(States._A_ADV);
        this.scorer.in(States._DEUCE).on(Events.B_Scores).goTo(States._B_ADV);

        this.scorer.in(States._A_ADV).executeOnEntry(from(this).setCurrentState(States._A_ADV));
        this.scorer.in(States._A_ADV).on(Events.A_Scores).goTo(States._A_GAME);
        this.scorer.in(States._A_ADV).on(Events.B_Scores).goTo(States._DEUCE);

        this.scorer.in(States._B_ADV).executeOnEntry(from(this).setCurrentState(States._B_ADV));
        this.scorer.in(States._B_ADV).on(Events.A_Scores).goTo(States._DEUCE);
        this.scorer.in(States._B_ADV).on(Events.B_Scores).goTo(States._B_GAME);

        this.scorer.in(States._A_GAME).executeOnEntry(from(this).setCurrentState(States._A_GAME));
        this.scorer.in(States._B_GAME).executeOnEntry(from(this).setCurrentState(States._B_GAME));
    }

    @After
    public void shutdown() {
        this.scorer.stop();
    }

    @Test
    public void testScorerWhenDeuceAndAScores2TimesThenAWins() {
        this.scorer.initialize(States._DEUCE);
        this.scorer.start();

        this.scorer.fire(Events.A_Scores);
        final States score1 = getCurrentState();

        this.scorer.fire(Events.A_Scores);
        final States score2 = getCurrentState();

        Assert.assertEquals(States._A_ADV, score1);
        Assert.assertEquals(States._A_GAME, score2);
    }

    @Test
    public void testScorerWhenDeuceAndB_AScoresThenDeuce() {
        this.scorer.initialize(States._DEUCE);
        this.scorer.start();

        this.scorer.fire(Events.A_Scores);
        final States score1 = getCurrentState();

        this.scorer.fire(Events.B_Scores);
        final States score2 = getCurrentState();
        ;

        Assert.assertEquals(States._A_ADV, score1);
        Assert.assertEquals(States._DEUCE, score2);
    }

    @Test
    public void testScorerWhenDeuceAndBScores2TimesThenBWins() {
        this.scorer.initialize(States._DEUCE);
        this.scorer.start();

        this.scorer.fire(Events.B_Scores);
        final States score1 = getCurrentState();

        this.scorer.fire(Events.B_Scores);
        final States score2 = getCurrentState();

        Assert.assertEquals(States._B_ADV, score1);
        Assert.assertEquals(States._B_GAME, score2);
    }

}
