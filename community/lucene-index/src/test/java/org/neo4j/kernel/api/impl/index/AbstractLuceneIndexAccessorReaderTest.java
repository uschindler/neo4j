/*
 * Copyright (c) 2002-2015 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.kernel.api.impl.index;

import java.io.Closeable;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public abstract class AbstractLuceneIndexAccessorReaderTest<R extends LuceneIndexAccessorReader>
{
    protected static final int BUFFER_SIZE_LIMIT = 100_000;

    protected final Closeable closeable = mock( Closeable.class );
    protected final LuceneDocumentStructure documentLogic = mock( LuceneDocumentStructure.class );
    protected final IndexSearcher searcher = mock( IndexSearcher.class );
    protected final IndexReader reader = mock( IndexReader.class );
    protected final TermEnum terms = mock( TermEnum.class );
    protected R accessor;


    @Test
    public void shouldUseCorrectLuceneQueryForSeekQuery() throws Exception
    {
        // Given
        when( documentLogic.newSeekQuery( "foo" ) ).thenReturn( mock( TermQuery.class) );

        // When
        accessor.seek( "foo" );

        // Then
        verify( documentLogic ).newSeekQuery( "foo" );
        verifyNoMoreInteractions( documentLogic );
    }

    @Test
    public void shouldUseCorrectLuceneQueryForRangeSeekByNumberQuery() throws Exception
    {
        // Given
        when( documentLogic.newRangeSeekByNumberQuery( 12, true, null, false ) ).thenReturn( mock( TermRangeQuery.class) );

        // When
        accessor.rangeSeekByNumber( 12, true, null, false );

        // Then
        verify( documentLogic ).newRangeSeekByNumberQuery( 12, true, null, false );
        verifyNoMoreInteractions( documentLogic );
    }

    @Test
    public void shouldUseCorrectLuceneQueryForRangeSeekByPrefixQuery() throws Exception
    {
        // Given
        when( documentLogic.newRangeSeekByPrefixQuery( "foo" ) ).thenReturn( mock( PrefixQuery.class) );

        // When
        accessor.rangeSeekByPrefix( "foo" );

        // Then
        verify( documentLogic ).newRangeSeekByPrefixQuery( "foo" );
        verifyNoMoreInteractions( documentLogic );
    }

    @Test
    public void shouldUseCorrectLuceneQueryForScanQuery() throws Exception
    {
        // Given
        when( documentLogic.newScanQuery() ).thenReturn( mock( MatchAllDocsQuery.class) );

        // When
        accessor.scan();

        // Then
        verify( documentLogic ).newScanQuery();
        verifyNoMoreInteractions( documentLogic );
    }
}