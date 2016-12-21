/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.accumulo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Objects.requireNonNull;

public class IndexColumn
{
    private final String column;

    @JsonCreator
    public IndexColumn(@JsonProperty("column") String column)
    {
        this.column = requireNonNull(column, "column is null");
    }

    @JsonProperty
    public String getColumn()
    {
        return column;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(column);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        IndexColumn other = (IndexColumn) obj;
        return Objects.equals(this.column, other.column);
    }

    @Override
    public String toString()
    {
        return toStringHelper(this).add("column", column).toString();
    }
}
