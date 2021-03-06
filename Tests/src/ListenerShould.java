import JavaREST.Framework.LogFormatter;
import JavaREST.Framework.Logger;
import JavaREST.Framework.IExecutorService;
import JavaREST.Framework.ServerSocketWrapper;
import JavaREST.Framework.Listener;
import com.sun.istack.internal.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Author: Keith Jackson
 * Date: 4/18/2016
 * License: MIT
 *
 */

public class ListenerShould {


    public class ListenerTest extends Listener {

        private boolean called = false;

        public ListenerTest(@NotNull ServerSocketWrapper wrapper,
                            @NotNull IExecutorService executor,
                            @NotNull Logger logger) {
            super(wrapper, executor, logger);
        }

        @Override
        public void handleRequest(Socket socket) {
            called = true;
        }

        public boolean handleRequestWasCalled() { return called; }
    }

    private ServerSocketWrapper serverSocketWrapper;
    private IExecutorService service;
    private ListenerTest listener;
    private Logger logger;


    @Before
    public void setup() {
        serverSocketWrapper = mock(ServerSocketWrapper.class);
        service = mock(IExecutorService.class);
        logger = new Logger(mock(OutputStream.class), mock(LogFormatter.class));
        listener = new ListenerTest(serverSocketWrapper, service, logger);

    }

    @After
    public void tearDown() {

    }

    @Test
    public void delegateIncomingConnections() throws IOException {

        // Arrange
        Mockito.doAnswer((Answer<Void>) invocationOnMock -> {
            ((Runnable)invocationOnMock.getArguments()[0]).run();
            throw new IOException();
        }).when(service).execute(any(Runnable.class));

        // Act
        listener.run();

        // Assert
        Mockito.verify(service, Mockito.atLeast(1)).execute(any(Runnable.class));
        Mockito.verify(serverSocketWrapper, Mockito.atLeast(1)).accept();
        Mockito.verify(service, Mockito.atLeast(1)).shutdown();
        assertThat(listener.handleRequestWasCalled(), is(true));
    }



}
