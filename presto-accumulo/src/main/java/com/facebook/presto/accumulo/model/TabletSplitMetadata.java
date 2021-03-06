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

import com.facebook.presto.accumulo.index.IndexQueryParameters;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.collect.ImmutableList;
import org.apache.accumulo.core.data.Range;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

public class TabletSplitMetadata
{
    private final List<Range> ranges;
    private final Collection<AccumuloRange> rowIdRanges;
    private final Optional<IndexQueryParameters> indexQueryParameters;

    @JsonCreator
    public TabletSplitMetadata(List<Range> ranges, Collection<AccumuloRange> rowIdRanges, Optional<IndexQueryParameters> indexQueryParameters)
    {
        this.ranges = ImmutableList.copyOf(requireNonNull(ranges, "ranges is null"));
        this.rowIdRanges = ImmutableList.copyOf(requireNonNull(rowIdRanges, "rowIdRanges is null"));
        this.indexQueryParameters = requireNonNull(indexQueryParameters, "indexQueryParameters is null");
        checkArgument(ranges.size() > 0 ^ indexQueryParameters.isPresent(), "Both ranges and index query parameters must not be set/empty");
    }

    public List<Range> getRanges()
    {
        return ranges;
    }

    public Collection<AccumuloRange> getRowIdRanges()
    {
        return rowIdRanges;
    }

    public Optional<IndexQueryParameters> getIndexQueryParameters()
    {
        return indexQueryParameters;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(ranges, rowIdRanges, indexQueryParameters);
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

        TabletSplitMetadata other = (TabletSplitMetadata) obj;
        return Objects.equals(this.ranges, other.ranges) &&
                Objects.equals(this.rowIdRanges, other.rowIdRanges) &&
                Objects.equals(this.indexQueryParameters, other.indexQueryParameters);
    }

    @Override
    public String toString()
    {
        return toStringHelper(this)
                .add("numRanges", ranges.size())
                .add("numRowIdRanges", rowIdRanges.size())
                .add("indexQueryParameters", indexQueryParameters)
                .toString();
    }
}
