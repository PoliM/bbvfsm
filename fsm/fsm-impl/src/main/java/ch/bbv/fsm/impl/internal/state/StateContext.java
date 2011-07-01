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
package ch.bbv.fsm.impl.internal.state;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * State Context.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 * @param <TState>
 * @param <TEvent>
 */
public class StateContext<TState, TEvent> {

    /**
     * A record of a state exit or entry. Used to log the way taken by
     * transitions and initialization.
     */
    public class Record {
        private TState stateId;
        private RecordType recordType;

        /**
         * Creates a new instance.
         * 
         * @param stateId
         *            the state id.
         * @param recordType
         *            the record type.
         */
        public Record(final TState stateId, final RecordType recordType) {
            this.stateId = stateId;
            this.recordType = recordType;
        }

        /**
         * Returns the record type.
         * 
         * @return the record type.
         */
        public RecordType getRecordType() {
            return this.recordType;
        }

        /**
         * Returns the state id.
         * 
         * @return the state id.
         */
        public TState getStateId() {
            return this.stateId;
        }

        /**
         * Sets the record type.
         * 
         * @param recordType
         *            the record type.
         */
        public void setRecordType(final RecordType recordType) {
            this.recordType = recordType;
        }

        /**
         * Sets the state id.
         * 
         * @param stateId
         *            the state id.
         */
        public void setStateId(final TState stateId) {
            this.stateId = stateId;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return this.recordType + " " + this.stateId;
        }
    }

    /**
     * Specifies the type of the record.
     */
    public enum RecordType {

        /**
         * A state was entered.
         */
        Enter,

        /**
         * A state was exited.
         */
        Exit
    }

    private final State<TState, TEvent> sourceState;

    /**
     * The exceptions that occurred during performing an operation.
     */
    private final List<Exception> exceptions;

    /**
     * The list of records (state exits, entries)
     */
    private final List<Record> records;

    /**
     * Creates a new instance.
     * 
     * @param sourceState
     *            the source state of the transition.
     */
    public StateContext(final State<TState, TEvent> sourceState) {
        this.sourceState = sourceState;
        this.exceptions = Lists.newArrayList();
        this.records = Lists.newArrayList();
    }

    /**
     * Adds a record.
     * 
     * @param stateId
     *            the state id.
     * @param recordType
     *            the record type.
     */
    public void addRecord(final TState stateId, final RecordType recordType) {
        this.records.add(new Record(stateId, recordType));
    }

    /**
     * Returns the occured exceptions during the transition.
     * 
     * @return the occured exceptions during the transition.
     */
    public List<Exception> getExceptions() {
        return this.exceptions;
    }

    /**
     * Returns all records in string representation.
     * 
     * @return all records in string representation.
     */
    public String getRecords() {
        final StringBuilder result = new StringBuilder();

        for (final Record record : this.records) {
            result.append(String.format(" -> %s", record));
        }

        return result.toString();
    }

    /**
     * Returns the source state of the transition.
     * 
     * @return the source state of the transition.
     */
    public State<TState, TEvent> getState() {
        return this.sourceState;
    }

}
