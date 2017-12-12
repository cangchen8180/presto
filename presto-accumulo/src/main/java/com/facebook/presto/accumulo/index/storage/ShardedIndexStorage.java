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
package com.facebook.presto.accumulo.index.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Bytes;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.abs;
import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * This storage strategy appends a zero-padded integer to the given byte array based on the byte array's hash code
 */
public class ShardedIndexStorage
        implements IndexStorage
{
    private int numShards;
    private int cropLength;
    private String formatString;

    @JsonCreator
    public ShardedIndexStorage(@JsonProperty("numShards") int numShards)
    {
        checkArgument(numShards > 1, "Number of shards must be greater than one");
        this.numShards = numShards;
        this.cropLength = Integer.toString(numShards - 1).length();
        this.formatString = "%0" + cropLength + "d";
    }

    @JsonProperty
    public int getNumShards()
    {
        return numShards;
    }

    @JsonIgnore
    public int getCropLength()
    {
        return cropLength;
    }

    @JsonIgnore
    public byte[] encode(byte[] bytes)
    {
        return Bytes.concat(format(formatString, abs(Arrays.hashCode(bytes)) % numShards).getBytes(UTF_8), bytes);
    }

    @JsonIgnore
    public byte[] decode(byte[] bytes)
    {
        return Arrays.copyOfRange(bytes, cropLength, bytes.length);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(numShards);
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

        ShardedIndexStorage other = (ShardedIndexStorage) obj;
        return Objects.equals(this.numShards, other.numShards);
    }

    @Override
    public String toString()
    {
        return toStringHelper(this).add("numShards", numShards).toString();
    }

    public List<byte[]> encodeAllShards(byte[] bytes)
    {
        ImmutableList.Builder<byte[]> byteBuilder = ImmutableList.builder();
        for (int i = 0; i < numShards; ++i) {
            byteBuilder.add(Bytes.concat(format(formatString, i).getBytes(UTF_8), bytes));
        }
        return byteBuilder.build();
    }
}