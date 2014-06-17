#import <Cordova/CDVPlugin.h>
/*
#import <PhoneGap/PGPlugin.h>
*/
@interface BinaryFileWriter : /*PGPlugin*/CDVPlugin {}

- (void)writeToFile:(NSMutableArray*)arguments withDict:(NSMutableDictionary*)options;
@end
