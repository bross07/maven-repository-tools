package org.apache.maven.archiva.meeper;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVParser;
import org.codehaus.plexus.util.IOUtil;

/**
 * Read a csv file with the synced repositories information
 *
 * @author <a href="mailto:carlos@apache.org">Carlos Sanchez</a>
 * @version $Id$
 */
public class CsvReader
{

    /**
     * @param is
     * @return {@link List} of {@link SyncedRepository}
     * @throws IOException
     */
    public List parse( InputStream is )
        throws IOException

    {
        InputStreamReader reader = new InputStreamReader( is );
        CSVParser parser = new CSVParser( reader );

        try
        {

            String[][] data = parser.getAllValues();
            List repos = new ArrayList( data.length - 1 );

            /* ignore headers line */
            for ( int i = 1; i < data.length; i++ )
            {
                int j = data[i].length - 1;
                SyncedRepository repo = new SyncedRepository();
                switch ( data[i].length )
                {
                    case 7:
                        repo.setSvnUrl( getValue( data[i][j--] ) );
                    case 6:
                        repo.setSshOptions( getValue( data[i][j--] ) );
                    case 5:
                        repo.setContactMail( getValue( data[i][j--] ) );
                    case 4:
                        repo.setContactName( getValue( data[i][j--] ) );
                    case 3:
                        repo.setProtocol( getValue( data[i][j--] ) );
                    case 2:
                        repo.setLocation( getValue( data[i][j--] ) );
                    case 1:
                        repo.setGroupId( getValue( data[i][j--] ) );
                        repos.add( repo );
                        break;
                    default:
                        // line ignored data[i];
                }
            }

            return repos;

        }
        finally
        {
            IOUtil.close( reader );
        }
    }

    private String getValue( String value )
    {
        if ( ( value == null ) || ( value.length() == 0 ) )
        {
            return null;
        }
        else
        {
            return value;
        }
    }
}
