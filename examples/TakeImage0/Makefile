# Build all the plans and scripts (XML and Maude).

PLANS   = TakeImage0
SCRIPTS = script0
PLX     = $(PLANS:=.plx)
PSX     = $(SCRIPTS:=.psx)
MAUDE   = $(PLX:.plx=.maude) $(PSX:.psx=.maude)

default: $(PLX) $(PSX) $(MAUDE)

clean:
	@ rm -f $(MAUDE) $(PLX) $(PSX)

%.plx : %.ple
	plexilc -p $<

%.maude : %.plx
	plx2maude $< > $@

%.psx : %.pst
	plexilc -p $<

%.maude : %.psx
	psx2maude $< > $@
