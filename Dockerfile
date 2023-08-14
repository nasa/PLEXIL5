FROM ubuntu:22.04

# PLEXIL
ENV TZ=Europe/Madrid
ENV PLEXIL_HOME=/plexil

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN apt-get update && apt-get install -y \
    git \
    make \
    autotools-dev \
    autoconf \
    libtool \
    g++ \
    ant \
    gperf \
    openjdk-8-jdk \
    freeglut3-dev \
    libxi-dev \
    libxmu-dev

RUN git clone https://git.code.sf.net/p/plexil/git plexil
RUN git clone https://github.com/plexil-rwl-support/semantic-tests.git semantic-tests

RUN echo "export PLEXIL_HOME=$PLEXIL_HOME" >> /root/.bashrc
RUN echo "export PATH=$PLEXIL_HOME/scripts:$PATH" >> /root/.bashrc
RUN echo "source $PLEXIL_HOME/scripts/plexil-setup.sh" >> /root/.bashrc

RUN ["/bin/bash", "-c", "source ~/.bashrc"]

RUN cd $PLEXIL_HOME && make src/configure
RUN cd $PLEXIL_HOME/src && ./configure --prefix=$PLEXIL_HOME --enable-ipc --enable-module-tests --enable-viewer --enable-sas --enable-test-exec --enable-udp

RUN cd $PLEXIL_HOME && grep -lR "#if defined(HAVE_CSTRING)" . | xargs sed -i s/"#if defined(HAVE_CSTRING)"/"#include <cstring>\n#if defined(HAVE_CSTRING)"/g

RUN cd $PLEXIL_HOME && make

#PLEXIL5
RUN apt-get update && apt-get install -y haskell-platform \
    && apt-get install -y ghc cabal-install curl unzip \
    && apt-get install -y libncurses5 \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

COPY Maude-3.1-linux.zip /Maude.zip

RUN unzip Maude.zip \
    && rm Maude.zip \
    && chmod +x maude-3.1/maude.linux64 \
    && ln -s /maude-3.1/maude.linux64 /usr/local/bin/maude

ENV PATH="/maude-3.1/bin:${PATH}"
ENV MAUDE_LIB="/maude-3.1"

RUN git clone https://github.com/plexil-rwl-support/PLEXIL5.git /static-plexil \
    && cd /static-plexil \
    && cabal update

RUN cd /static-plexil/plexil2maude \
    && cabal v2-install \
    && cd ../plexilog \
    && cabal v2-install