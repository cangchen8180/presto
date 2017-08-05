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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.hadoop.io.Text;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Objects.requireNonNull;

public class IndexColumn
{
    private final List<String> columns;
    private final String tableName;
    private Map<String, Set<Text>> localityGroups;

    @JsonCreator
    public IndexColumn(
            @JsonProperty("columns") List<String> columns,
            @JsonProperty("tableName") String tableName,
            @JsonProperty("localityGroups") Map<String, Set<Text>> localityGroups)
    {
        this.columns = requireNonNull(columns, "column is null");
        this.tableName = requireNonNull(tableName, "tableName is null");
        this.localityGroups = requireNonNull(localityGroups, "localityGroups is null");
    }

    @JsonProperty
    public List<String> getColumns()
    {
        return columns;
    }

    @JsonProperty
    public String getTableName()
    {
        return tableName;
    }

    @JsonProperty
    public Map<String, Set<Text>> getLocalityGroups()
    {
        return localityGroups;
    }

    @JsonIgnore
    public int getNumColumns()
    {
        return columns.size();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(columns);
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
        return Objects.equals(this.columns, other.columns);
    }

    @Override
    public String toString()
    {
        return toStringHelper(this).add("tableName", tableName).add("columns", columns).toString();
    }
}
