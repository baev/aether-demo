/*******************************************************************************
 * Copyright (c) 2010, 2014 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sonatype, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.aether.examples.util;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.Authentication;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.Proxy;
import org.eclipse.aether.repository.ProxySelector;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.util.repository.AuthenticationBuilder;
import org.eclipse.aether.util.repository.DefaultProxySelector;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A helper to boot the repository system and a repository system session.
 */
public class Booter {

    public static RepositorySystem newRepositorySystem() {
        return org.eclipse.aether.examples.manual.ManualRepositorySystemFactory.newRepositorySystem();
        // return org.eclipse.aether.examples.guice.GuiceRepositorySystemFactory.newRepositorySystem();
        // return org.eclipse.aether.examples.sisu.SisuRepositorySystemFactory.newRepositorySystem();
        // return org.eclipse.aether.examples.plexus.PlexusRepositorySystemFactory.newRepositorySystem();
    }

    public static DefaultRepositorySystemSession newRepositorySystemSession(RepositorySystem system) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

        LocalRepository localRepo = new LocalRepository("target/local-repo");
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));

        session.setTransferListener(new ConsoleTransferListener());
        session.setRepositoryListener(new ConsoleRepositoryListener());

        session.setProxySelector(newProxySelector("http", "asdasd", 80));

        // uncomment to generate dirty trees
        // session.setDependencyGraphTransformer( null );

        return session;
    }

    public static DefaultRepositorySystemSession newRepositorySystemSession(RepositorySystem system, File localRepoDir, String proxyHost, int port) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

        LocalRepository localRepo = new LocalRepository(localRepoDir);
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));

        session.setTransferListener(new ConsoleTransferListener());
        session.setRepositoryListener(new ConsoleRepositoryListener());

        session.setProxySelector(newProxySelector("http", proxyHost, port));

        // uncomment to generate dirty trees
        // session.setDependencyGraphTransformer( null );

        return session;
    }

    public static List<RemoteRepository> newRepositories(RepositorySystem system, RepositorySystemSession session) {
        return new ArrayList<RemoteRepository>(Arrays.asList(toRemoteRepository("central", "default", "http://central.maven.org/maven2/", session)));
    }

    private static RemoteRepository newCentralRepository() {
        return new RemoteRepository.Builder("central", "default", "http://central.maven.org/maven2/").build();
    }

    public static RemoteRepository toRemoteRepository(String id, String type, String url, RepositorySystemSession session) {
        RemoteRepository.Builder prototypeBuilder =
                new RemoteRepository.Builder(id, type, url);
        RemoteRepository prototype = prototypeBuilder.build();
        RemoteRepository.Builder builder = new RemoteRepository.Builder(prototype);
        builder.setAuthentication(session.getAuthenticationSelector().getAuthentication(prototype));
        builder.setProxy(session.getProxySelector().getProxy(prototype));
        builder.setMirroredRepositories(Arrays.asList(session.getMirrorSelector().getMirror(prototype)));
        return builder.build();
    }

    public static ProxySelector newProxySelector(String protocol, String host, int port) {
        DefaultProxySelector proxySelector = new DefaultProxySelector();
        Authentication auth = new AuthenticationBuilder()
                .addUsername("")
                .addPassword("")
                .build();
        proxySelector.add(new Proxy(
                        protocol,
                        host,
                        port,
                        auth),
                ""
        );
        return proxySelector;
    }
}
