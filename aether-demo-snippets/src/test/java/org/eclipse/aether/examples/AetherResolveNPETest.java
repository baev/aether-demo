package org.eclipse.aether.examples;

import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.examples.util.Booter;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.util.artifact.JavaScopes;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.eclipse.aether.examples.util.Booter.newRepositories;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 14.11.14
 */
@RunWith(Parameterized.class)
public class AetherResolveNPETest {

    private String host;
    private int port;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    public AetherResolveNPETest(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[]{"invalid-a", 80},
                new Object[]{"18.0.3.1", 8080},
                new Object[]{"290.0.3.1", 80}
        );
    }

    @Test
    public void resolveNPETest() throws Exception {
        RepositorySystem system = Booter.newRepositorySystem();

        DefaultRepositorySystemSession session = Booter.newRepositorySystemSession(system, folder.newFolder(), host, port);

        Artifact artifact = new DefaultArtifact("org.eclipse.aether:aether-util:1.0.0.v20140518");

        CollectRequest collectRequest = new CollectRequest(
                new Dependency(artifact, JavaScopes.RUNTIME),
                newRepositories(system, session)
        );

        DependencyRequest request = new DependencyRequest(collectRequest, null);
        system.resolveDependencies(session, request).getArtifactResults();
    }


}
