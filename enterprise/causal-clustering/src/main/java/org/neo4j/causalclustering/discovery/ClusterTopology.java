/*
 * Copyright (c) 2002-2017 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.causalclustering.discovery;

import java.util.Optional;

import org.neo4j.causalclustering.identity.MemberId;

public class ClusterTopology
{
    private final CoreTopology coreTopology;
    private final ReadReplicaTopology readReplicaTopology;

    public ClusterTopology( CoreTopology coreTopology, ReadReplicaTopology readReplicaTopology )
    {
        this.coreTopology = coreTopology;
        this.readReplicaTopology = readReplicaTopology;
    }

    public Optional<CatchupServerAddress> find( MemberId upstream )
    {
        Optional<CoreAddresses> coreAddresses = coreTopology.find( upstream );
        Optional<ReadReplicaAddresses> readReplicaAddresses = readReplicaTopology.findAddressFor( upstream );

        if ( coreAddresses.isPresent() )
        {
            return Optional.of( coreAddresses.get() );
        }
        else if ( readReplicaAddresses.isPresent() )
        {
            return Optional.of( readReplicaAddresses.get() );
        }
        else
        {
            return Optional.empty();
        }
    }
}
