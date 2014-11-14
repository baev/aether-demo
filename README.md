## Sample project for Aether bug-451566

NPE when specify incorrect proxy in ProxySelector

See https://bugs.eclipse.org/bugs/show_bug.cgi?id=451566

### How to reproduce

I add a test to reproduce the problem. See `org.eclipse.aether.examples.AetherResolveNPETest`. Just run `mvn test`

Project changes:
* org.eclipse.aether.examples.util.Booter - provide proxy selector with proxy settings to session and remote repository