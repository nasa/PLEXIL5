FROM ubuntu:22.04

ENV TZ=Europe/Madrid
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

# PLEXIL
ENV PLEXIL_HOME=/plexil
RUN git clone https://github.com/plexil-group/plexil.git plexil --branch releases/plexil-6
WORKDIR /plexil
RUN make ipc
RUN make tools -j


# PLEXIL5
RUN apt-get install -y curl unzip libgmp-dev libz-dev

ENV BOOTSTRAP_HASKELL_NONINTERACTIVE=true
ENV BOOTSTRAP_HASKELL_GHC_VERSION=9.6.7
ENV BOOTSTRAP_HASKELL_INSTALL_NO_STACK=true
RUN curl --proto '=https' --tlsv1.2 -sSf https://get-ghcup.haskell.org | sh
ENV PATH="/root/.ghcup/bin/:${PATH}"

## Cabal prefetch
RUN cabal update
RUN mkdir /bootstrap-cabal

COPY plexilog /bootstrap-cabal/plexilog
COPY plexil2maude /bootstrap-cabal/plexil2maude

WORKDIR /bootstrap-cabal/plexilog
RUN cabal build --only-dependencies

WORKDIR /bootstrap-cabal/plexil2maude
RUN cabal build --only-dependencies

## Maude
ENV MAUDE_PATH=/Linux64
ENV PATH="$MAUDE_PATH/bin:${PATH}"
ENV MAUDE_LIB="$MAUDE_PATH"
WORKDIR /
COPY Maude-3.1-linux.zip /Maude.zip
RUN unzip Maude.zip \
    && rm Maude.zip \
    && chmod +x $MAUDE_PATH/maude.linux64 \
    && ln -s $MAUDE_PATH/maude.linux64 /usr/local/bin/maude

WORKDIR /
